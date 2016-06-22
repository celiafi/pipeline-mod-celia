import static org.daisy.pipeline.pax.exam.Options.brailleModule;
import static org.daisy.pipeline.pax.exam.Options.calabashConfigFile;
import static org.daisy.pipeline.pax.exam.Options.domTraversalPackage;
import static org.daisy.pipeline.pax.exam.Options.felixDeclarativeServices;
import static org.daisy.pipeline.pax.exam.Options.logbackClassic;
import static org.daisy.pipeline.pax.exam.Options.logbackConfigFile;
import static org.daisy.pipeline.pax.exam.Options.mavenBundle;
import static org.daisy.pipeline.pax.exam.Options.mavenBundlesWithDependencies;
import static org.daisy.pipeline.pax.exam.Options.pipelineModule;
import static org.daisy.pipeline.pax.exam.Options.thisBundle;
import static org.daisy.pipeline.pax.exam.Options.xprocspec;

import org.ops4j.pax.exam.Option;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;

public class Config {
	
	public static Option[] config() {
		return options(
			logbackConfigFile(),
			domTraversalPackage(),
			felixDeclarativeServices(),
			calabashConfigFile(),
			thisBundle(),
			junitBundles(),
			mavenBundlesWithDependencies(
				brailleModule("common-utils"),
				brailleModule("css-utils"),
				brailleModule("liblouis-utils"),
				brailleModule("liblouis-tables"),
				brailleModule("liblouis-native").forThisPlatform(),
				brailleModule("libhyphen-core"),
				brailleModule("libhyphen-native").forThisPlatform(),
				brailleModule("texhyph-core"),
				brailleModule("pef-utils"),
				brailleModule("dotify-utils"),
				brailleModule("dotify-formatter"),
				brailleModule("dtbook-to-pef"),
				logbackClassic(),
				xprocspec(),
				mavenBundle("org.daisy.maven:xproc-engine-daisy-pipeline:?"))
			);
	}
}
