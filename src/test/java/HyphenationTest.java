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

import net.davidashen.text.Hyphenator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.net.URI;
import org.daisy.pipeline.braille.ResourceResolver;
import java.net.URL;
import static org.daisy.pipeline.braille.Utilities.URLs.asURL;

//@RunWith(PaxExam.class)
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
	Hyphenator hyph;
	
	@Test
	public void testWithTexhyph() {
		hyph = new Hyphenator();
		try {
			File file = new File("src/main/resources/hyph/hyph-fi");
			FileInputStream fileStream = new FileInputStream(file);
			InputStreamReader streamReader = new InputStreamReader(fileStream);
			hyph.loadTable(streamReader);
		}
		catch(java.io.FileNotFoundException e) {
		}
		catch(net.davidashen.text.Utf8TexParser.TexParserException e) {
		}
		assertEquals("au\u00ADto", hyph.hyphenate("auto"));
	}
	
/*	@Configuration
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
	}*/
	
	private static boolean onWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
	
}
