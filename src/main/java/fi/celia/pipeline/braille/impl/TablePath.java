package fi.celia.pipeline.braille.impl;

import java.util.Map;

import org.daisy.pipeline.braille.liblouis.LiblouisTablePath;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.ComponentContext;

@Component(
	name = "fi.celia.pipeline.braille.impl.TablePath",
	service = {
		LiblouisTablePath.class
	},
	property = {
		"identifier:String=http://www.celia.fi/liblouis",
		"path:String=/liblouis"
	}
)

public class TablePath extends LiblouisTablePath {
	
	@Activate
	protected void activate(ComponentContext context, Map<?,?> properties) throws Exception {
		super.activate(context, properties);
	}
}
