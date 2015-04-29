import javax.inject.Inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.davidashen.text.Hyphenator;
import net.davidashen.text.Utf8TexParser.TexParserException;

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
		assertEquals("nä\u00ADen", texhyph.hyphenate(asURI("hyph-fi.tex"), "näen"));
	}
	
	@Test
	public void testWithPlainTexhyph() throws FileNotFoundException, UnsupportedEncodingException, TexParserException {
		Hyphenator hyph = new Hyphenator();
		File file = new File("target/generated-resources/hyph/hyph-fi.tex");
		FileInputStream fileStream = new FileInputStream(file);
		InputStreamReader streamReader = new InputStreamReader(fileStream, "UTF-8");
		hyph.loadTable(streamReader);
		assertEquals("Simple hyphenation", "au\u00ADto", hyph.hyphenate("auto"));
		assertEquals("Diphthong", "näin", hyph.hyphenate("näin"));
		assertEquals("Dual vowel", "nä\u00ADen", hyph.hyphenate("näen"));
		assertEquals("Vowel triplet", "ka\u00ADvi\u00ADaa\u00ADri", hyph.hyphenate("kaviaari"));
		assertEquals("Compound word with hyphen", "rek\u00ADka-\u200Bau\u00ADto", hyph.hyphenate("rekka-auto"));
		assertEquals("Native consonant cluster", "kars\u00ADta", hyph.hyphenate("karsta"));
		assertEquals("Borrowed consonant cluster", "verk\u00ADko\u00ADstra\u00ADte\u00ADgi\u00ADa", hyph.hyphenate("verkkostrategia"));
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
