package fi.celia.pipeline.braille.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.net.URI;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.runtime.XAtomicStep;

import org.daisy.common.file.URLs;
import org.daisy.common.xproc.calabash.XProcStep;
import org.daisy.common.xproc.calabash.XProcStepProvider;
import org.daisy.pipeline.braille.common.AbstractBrailleTranslator;
import org.daisy.pipeline.braille.common.AbstractTransformProvider;
import org.daisy.pipeline.braille.common.AbstractTransformProvider.util.Function;
import org.daisy.pipeline.braille.common.AbstractTransformProvider.util.Iterables;
import org.daisy.pipeline.braille.common.calabash.CxEvalBasedTransformer;
import static org.daisy.pipeline.braille.common.AbstractTransformProvider.util.logCreate;
import static org.daisy.pipeline.braille.common.AbstractTransformProvider.util.logSelect;
import org.daisy.pipeline.braille.common.BrailleTranslator;
import org.daisy.pipeline.braille.common.BrailleTranslatorProvider;
import org.daisy.pipeline.braille.common.CSSStyledText;
import org.daisy.pipeline.braille.common.Query;
import org.daisy.pipeline.braille.common.Query.Feature;
import org.daisy.pipeline.braille.common.Query.MutableQuery;
import static org.daisy.pipeline.braille.common.Query.util.mutableQuery;
import org.daisy.pipeline.braille.common.TransformProvider;
import static org.daisy.pipeline.braille.common.TransformProvider.util.dispatch;
import static org.daisy.pipeline.braille.common.TransformProvider.util.memoize;
import static org.daisy.pipeline.braille.common.util.Locales.parseLocale;
import org.daisy.pipeline.braille.libhyphen.LibhyphenHyphenator;
import org.daisy.pipeline.braille.liblouis.LiblouisTranslator;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

public interface CeliaTranslator {
	
	@Component(
		name = "fi.celia.pipeline.braille.impl.CeliaTranslator.Provider",
		service = {
			TransformProvider.class,
			BrailleTranslatorProvider.class
		}
	)
	public class Provider extends AbstractTransformProvider<BrailleTranslator> implements BrailleTranslatorProvider<BrailleTranslator> {
		
		private URI href;
		
		@Activate
		void activate(final Map<?,?> properties) {
			href = URLs.asURI(URLs.getResourceFromJAR("/xml/block-translate.xpl", CeliaTranslator.class));
		}
		
		private final static Query liblouisTable = mutableQuery().add("liblouis-table", "http://www.liblouis.org/tables/fi.utb,http://www.celia.fi/liblouis/undefined.utb,http://www.celia.fi/liblouis/additional-symbols.utb");
		private final static Query hyphenTable = mutableQuery().add("libhyphen-table", "http://www.celia.fi/pipeline/hyphen/hyph-fi.dic");
		
		private final static Iterable<BrailleTranslator> empty = Iterables.<BrailleTranslator>empty();
		
		private final static List<String> supportedInput = ImmutableList.of("css","text-css","dtbook","html");
		private final static List<String> supportedOutput = ImmutableList.of("css", "braille");
		
		/**
		 * Recognized features:
		 *
		 * - translator: Will only match if the value is 'celia'.
		 * - locale: Will only match if the language subtag is 'fi'.
		 *
		 */
		protected Iterable<BrailleTranslator> _get(Query query) {
			final MutableQuery q = mutableQuery(query);
			for (Feature f : q.removeAll("input"))
				if (!supportedInput.contains(f.getValue().get()))
					return empty;
			for (Feature f : q.removeAll("output"))
				if (!supportedOutput.contains(f.getValue().get()))
					return empty;
			if (q.containsKey("locale"))
				if (!"fi".equals(parseLocale(q.removeOnly("locale").getValue().get()).getLanguage()))
					return empty;
			if (q.containsKey("translator"))
				if ("celia".equals(q.removeOnly("translator").getValue().get()))
					if (q.isEmpty()) {
						Iterable<LibhyphenHyphenator> hyphenators = logSelect(hyphenTable, libhyphenHyphenatorProvider);
						return Iterables.concat(
							Iterables.transform(
								hyphenators,
								new Function<LibhyphenHyphenator,Iterable<BrailleTranslator>>() {
									public Iterable<BrailleTranslator> _apply(LibhyphenHyphenator h) {
										Query translatorQuery = mutableQuery(liblouisTable).add("hyphenator", h.getIdentifier());
										return Iterables.transform(
											logSelect(translatorQuery, liblouisTranslatorProvider),
											new Function<LiblouisTranslator,BrailleTranslator>() {
												public BrailleTranslator _apply(LiblouisTranslator translator) {
													return __apply(logCreate(new TransformImpl(translator))); }}); }})); }
			return empty;
		}
		
		private class TransformImpl extends AbstractBrailleTranslator implements XProcStepProvider {
			
			private final FromStyledTextToBraille translator;
			private final Map<String,String> options;
			
			private TransformImpl(LiblouisTranslator translator) {
				options = ImmutableMap.of("query", mutableQuery().add("id", this.getIdentifier()).toString());
				this.translator = translator.fromStyledTextToBraille();
			}
			
			@Override
			public XProcStep newStep(XProcRuntime runtime, XAtomicStep step) {
				return new CxEvalBasedTransformer(href, null, options).newStep(runtime, step);
			}
			
			@Override
			public FromStyledTextToBraille fromStyledTextToBraille() {
				return fromStyledTextToBraille;
			}
			
			private final FromStyledTextToBraille fromStyledTextToBraille = new FromStyledTextToBraille() {
				public java.lang.Iterable<String> transform(java.lang.Iterable<CSSStyledText> styledText, int from, int to) {
					return translator.transform(styledText, from, to);
				}
			};
			
			@Override
			public String toString() {
				return MoreObjects.toStringHelper(CeliaTranslator.class.getSimpleName())
					.add("id", getIdentifier())
					.toString();
			}
		}
		
		@Reference(
			name = "LiblouisTranslatorProvider",
			unbind = "unbindLiblouisTranslatorProvider",
			service = LiblouisTranslator.Provider.class,
			cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC
		)
		protected void bindLiblouisTranslatorProvider(LiblouisTranslator.Provider provider) {
			liblouisTranslatorProviders.add(provider);
		}
	
		protected void unbindLiblouisTranslatorProvider(LiblouisTranslator.Provider provider) {
			liblouisTranslatorProviders.remove(provider);
			liblouisTranslatorProvider.invalidateCache();
		}
	
		private List<TransformProvider<LiblouisTranslator>> liblouisTranslatorProviders
		= new ArrayList<TransformProvider<LiblouisTranslator>>();
		private TransformProvider.util.MemoizingProvider<LiblouisTranslator> liblouisTranslatorProvider
		= memoize(dispatch(liblouisTranslatorProviders));
		
		@Reference(
			name = "LibhyphenHyphenatorProvider",
			unbind = "unbindLibhyphenHyphenatorProvider",
			service = LibhyphenHyphenator.Provider.class,
			cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC
			)
			protected void bindLibhyphenHyphenatorProvider(LibhyphenHyphenator.Provider provider) {
			libhyphenHyphenatorProviders.add(provider);
		}
		
		protected void unbindLibhyphenHyphenatorProvider(LibhyphenHyphenator.Provider provider) {
			libhyphenHyphenatorProviders.remove(provider);
			libhyphenHyphenatorProvider.invalidateCache();
		}
		
		private List<TransformProvider<LibhyphenHyphenator>> libhyphenHyphenatorProviders
		= new ArrayList<TransformProvider<LibhyphenHyphenator>>();
		private TransformProvider.util.MemoizingProvider<LibhyphenHyphenator> libhyphenHyphenatorProvider
		= memoize(dispatch(libhyphenHyphenatorProviders));
		
	}
}
