package fi.celia.pipeline.braille.impl;

import java.util.Map;

import org.daisy.pipeline.braille.liblouis.LiblouisTablePath;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

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
	public void activate(Map<?,?> properties) {
		super.activate(properties, TablePath.class);
	}
}
