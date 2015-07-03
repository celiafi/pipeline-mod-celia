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
import static org.daisy.pipeline.braille.common.Provider.util.memoize;
import org.daisy.pipeline.braille.common.Transform;
import static org.daisy.pipeline.braille.common.Transform.Provider.util.dispatch;
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
		
		public Transform.Provider<CeliaCSSBlockTransform> withContext(Logger context) {
			return this;
		}
		
		/**
		 * Recognized features:
		 *
		 * - translator: Will only match if the value is `celia'.
		 *
		 */
		public Iterable<CeliaCSSBlockTransform> get(String query) {
			Map<String,Optional<String>> q = new HashMap<String,Optional<String>>(parseQuery(query));
			Optional<String> o;
			if ((o = q.remove("translator")) != null)
				if (o.get().equals("celia"))
					if (q.isEmpty()) {
						if (instance == null)
							try {
								instance = Optional.of((CeliaCSSBlockTransform)new TransformImpl()).asSet(); }
							catch (Exception e) {
								return empty; }
						return instance; }
			return empty;
		}
		
		private Iterable<CeliaCSSBlockTransform> instance;
		private static final Iterable<CeliaCSSBlockTransform> empty = Optional.<CeliaCSSBlockTransform>absent().asSet();
		
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
		
		private class TransformImpl implements CeliaCSSBlockTransform {
			
			private final static String query = "(liblouis-table:'http://www.liblouis.org/tables/fi.utb')";
			private final Tuple3<URI,QName,Map<String,String>> xproc;
	
			private TransformImpl() {
				try {
					LiblouisTranslator translator = liblouisTranslatorProvider.get(query).iterator().next(); }
				catch (NoSuchElementException e) {
					throw new RuntimeException(); }
				Map<String,String> options = ImmutableMap.of("query", query);
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
