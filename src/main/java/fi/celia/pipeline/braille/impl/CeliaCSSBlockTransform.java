package fi.celia.pipeline.braille.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.net.URI;
import javax.xml.namespace.QName;

import static com.google.common.base.Objects.toStringHelper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import static org.daisy.pipeline.braille.css.Query.parseQuery;
import static org.daisy.pipeline.braille.common.util.Tuple3;
import static org.daisy.pipeline.braille.common.util.URIs.asURI;
import org.daisy.pipeline.braille.common.CSSBlockTransform;
import org.daisy.pipeline.braille.common.LazyValue.ImmutableLazyValue;
import static org.daisy.pipeline.braille.common.Provider.util.memoize;
import org.daisy.pipeline.braille.common.Transform;
import org.daisy.pipeline.braille.common.Transform.AbstractTransform;
import org.daisy.pipeline.braille.common.Transform.Provider.AbstractProvider;
import static org.daisy.pipeline.braille.common.Transform.Provider.util.dispatch;
import static org.daisy.pipeline.braille.common.Transform.Provider.util.logCreate;
import static org.daisy.pipeline.braille.common.Transform.Provider.util.logSelect;
import org.daisy.pipeline.braille.common.WithSideEffect;
import org.daisy.pipeline.braille.common.XProcTransform;
import org.daisy.pipeline.braille.liblouis.LiblouisTranslator;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.ComponentContext;

import org.slf4j.Logger;

public interface CeliaCSSBlockTransform extends CSSBlockTransform, XProcTransform {
	
	@Component(
		name = "fi.celia.pipeline.braille.impl.CeliaCSSBlockTransform.Provider",
		service = {
			XProcTransform.Provider.class,
			CSSBlockTransform.Provider.class
		}
	)
	public class Provider implements XProcTransform.Provider<CeliaCSSBlockTransform>, CSSBlockTransform.Provider<CeliaCSSBlockTransform> {
		
		private URI href;
		
		@Activate
		private void activate(ComponentContext context, final Map<?,?> properties) {
			href = asURI(context.getBundleContext().getBundle().getEntry("xml/block-translate.xpl"));
		}
		
		/**
		 * Recognized features:
		 *
		 * - translator: Will only match if the value is `celia'.
		 *
		 */
		public Iterable<CeliaCSSBlockTransform> get(String query) {
			 return impl.get(query);
		 }
	
		public Transform.Provider<CeliaCSSBlockTransform> withContext(Logger context) {
			return impl.withContext(context);
		}
	
		private Transform.Provider.MemoizingProvider<CeliaCSSBlockTransform> impl = new ProviderImpl(null);
	
		private class ProviderImpl extends AbstractProvider<CeliaCSSBlockTransform> {
			
			private final static String liblouisTable = "(liblouis-table:'http://www.liblouis.org/tables/fi.utb')";
			
			private ProviderImpl(Logger context) {
				super(context);
			}
			
			protected Transform.Provider.MemoizingProvider<CeliaCSSBlockTransform> _withContext(Logger context) {
				return new ProviderImpl(context);
			}
			
			protected Iterable<WithSideEffect<CeliaCSSBlockTransform,Logger>> __get(final String query) {
				return new ImmutableLazyValue<WithSideEffect<CeliaCSSBlockTransform,Logger>>() {
					public WithSideEffect<CeliaCSSBlockTransform,Logger> _apply() {
						return new WithSideEffect<CeliaCSSBlockTransform,Logger>() {
							public CeliaCSSBlockTransform _apply() {
								Map<String,Optional<String>> q = new HashMap<String,Optional<String>>(parseQuery(query));
								Optional<String> o;
								if ((o = q.remove("translator")) != null)
									if (o.get().equals("celia"))
										if (q.size() == 0) {
											try {
												applyWithSideEffect(
													logSelect(
														liblouisTable,
														liblouisTranslatorProvider.get(liblouisTable)).iterator().next()); }
											catch (NoSuchElementException e) {
												throw new NoSuchElementException(); }
											return applyWithSideEffect(
												logCreate(
													(CeliaCSSBlockTransform)new TransformImpl(liblouisTable))); }
								throw new NoSuchElementException();
							}
						};
					}
				};
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
	
		private List<Transform.Provider<LiblouisTranslator>> liblouisTranslatorProviders
		= new ArrayList<Transform.Provider<LiblouisTranslator>>();
		private org.daisy.pipeline.braille.common.Provider.MemoizingProvider<String,LiblouisTranslator> liblouisTranslatorProvider
		= memoize(dispatch(liblouisTranslatorProviders));
		
		private class TransformImpl extends AbstractTransform implements CeliaCSSBlockTransform {
			
			private final Tuple3<URI,QName,Map<String,String>> xproc;
	
			private TransformImpl(String translatorQuery) {
				Map<String,String> options = ImmutableMap.of("query", translatorQuery);
				xproc = new Tuple3<URI,QName,Map<String,String>>(href, null, options);
			}
	
			public Tuple3<URI,QName,Map<String,String>> asXProc() {
				return xproc;
			}
	
			@Override
			public String toString() {
				return toStringHelper(CeliaCSSBlockTransform.class.getSimpleName())
					.toString();
			}
		}
	}
}
