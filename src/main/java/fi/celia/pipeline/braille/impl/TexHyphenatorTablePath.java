package fi.celia.pipeline.braille.impl;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.ComponentContext;

@Component(
	name = "fi.celia.pipeline.braille.impl.TexHyphenatorTablePath",
	service = {
		org.daisy.pipeline.braille.tex.TexHyphenatorTablePath.class
	},
	property = {
		"identifier:String=http://www.celia.fi/pipeline/texhyph/",
		"path:String=/hyph",
		"includes:String={*.tex,*.properties}"
	}
)
public class TexHyphenatorTablePath extends org.daisy.pipeline.braille.tex.TexHyphenatorTablePath {
	
	@Activate
	protected void activate(ComponentContext context, Map<?,?> properties) throws Exception {
		super.activate(context, properties);
	}
}
