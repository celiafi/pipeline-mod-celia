import javax.inject.Inject;

import org.daisy.pipeline.braille.libhyphen.Libhyphen;
import org.daisy.pipeline.braille.tex.TexHyphenator;
import static org.daisy.pipeline.braille.Utilities.URIs.asURI;

import static org.daisy.pipeline.pax.exam.Options.brailleModule;
import static org.daisy.pipeline.pax.exam.Options.felixDeclarativeServices;
import static org.daisy.pipeline.pax.exam.Options.forThisPlatform;
import static org.daisy.pipeline.pax.exam.Options.logbackBundles;
import static org.daisy.pipeline.pax.exam.Options.logbackConfigFile;
import static org.daisy.pipeline.pax.exam.Options.thisBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.PathUtils;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.osgi.framework.BundleContext;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class HyphenationTest {
	
	@Inject
	BundleContext context;
	
	@Test
	public void testWithLibhyphen() {
		assumeTrue(! onWindows);
		Libhyphen libhyphen = (Libhyphen)context.getService(context.getServiceReference(Libhyphen.class.getName()));;
		assertEquals("au\u00ADto", libhyphen.hyphenate(asURI("hyph-fi.dic"), "auto"));
	}
	
	@Inject
	TexHyphenator texhyph;
	
	@Test
	public void testWithTexhyph() {
		assertEquals("au\u00ADto", texhyph.hyphenate(asURI("hyph-fi.tex"), "auto"));
	}
	
	@Configuration
	public Option[] config() {
		return options(
			logbackConfigFile(),
			logbackBundles(),
			felixDeclarativeServices(),
			mavenBundle().groupId("com.google.guava").artifactId("guava").versionAsInProject(),
			mavenBundle().groupId("net.java.dev.jna").artifactId("jna").versionAsInProject(),
			mavenBundle().groupId("org.daisy.bindings").artifactId("jhyphen").versionAsInProject(),
			mavenBundle().groupId("com.googlecode.texhyphj").artifactId("texhyphj").versionAsInProject(),
			brailleModule("common-java"),
			brailleModule("libhyphen-core"),
			onWindows ? null : forThisPlatform(brailleModule("libhyphen-native")),
			brailleModule("texhyph-core"),
			thisBundle(true),
			junitBundles()
		);
	}
	
	private static boolean onWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
	
}
