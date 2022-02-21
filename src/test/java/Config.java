import static org.daisy.pipeline.pax.exam.Options.thisPlatform;

public class Config {
	static String[] testDependencies() {
		return new String[] {
			"org.daisy.pipeline.modules:css-utils:?",
			"org.daisy.pipeline.modules.braille:common-utils:?",
			"org.daisy.pipeline.modules.braille:css-utils:?",
			"org.daisy.pipeline.modules.braille:liblouis-utils:?",
			"org.daisy.pipeline.modules.braille:liblouis-utils:jar:" + thisPlatform() + ":?",
			"org.daisy.pipeline.modules.braille:libhyphen-utils:?",
			"org.daisy.pipeline.modules.braille:libhyphen-utils:jar:" + thisPlatform() + ":?",
			"org.daisy.pipeline.modules.braille:texhyph-utils:?",
			"org.daisy.pipeline.modules.braille:pef-utils:?",
			"org.daisy.pipeline.modules.braille:dotify-utils:?",
			"org.daisy.pipeline.modules.braille:dtbook-to-pef:?",
		};
	}
}
