import javax.inject.Inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;

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
import org.junit.BeforeClass;

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
	
	static ArrayList<String[]> testCases;
	
	@BeforeClass
	public static void initializeTestCases(){
		testCases = new ArrayList<String[]>();
		testCases.add(new String[]{"Simple hyphenation", "au\u00ADto", "auto"});
		testCases.add(new String[]{"Diphthong", "näin", "näin"});
		testCases.add(new String[]{"Dual vowel", "nä\u00ADen", "näen"});
		testCases.add(new String[]{"Vowel triplet", "ka\u00ADvi\u00ADaa\u00ADri", "kaviaari"});
		testCases.add(new String[]{"Compound word with hyphen", "rek\u00ADka-\u200Bau\u00ADto", "rekka-auto"});
		testCases.add(new String[]{"Native consonant cluster", "kars\u00ADta", "karsta"});
		testCases.add(new String[]{"Borrowed consonant cluster", "verk\u00ADko\u00ADstra\u00ADte\u00ADgi\u00ADa", "verkkostrategia"});
		testCases.add(new String[]{"Non-word-ending and word-ending s", "os\u00ADtos", "ostos"});
	}
	
	@Inject
	BundleContext context;
	
	@Test
	public void testWithLibhyphen() {
		assumeTrue(! onWindows);
		Libhyphen libhyphen = (Libhyphen)context.getService(context.getServiceReference(Libhyphen.class.getName()));;
		for(String[] testCase : testCases) {
			assertEquals(testCase[0], testCase[1], libhyphen.hyphenate(asURI("hyph-fi.dic"), testCase[2]));
		}
	}
	
	@Inject
	TexHyphenator texhyph;
	
	@Test
	public void testWithTexhyph() {
		for(String[] testCase : testCases) {
			assertEquals(testCase[0], testCase[1], texhyph.hyphenate(asURI("hyph-fi.tex"), testCase[2]));
		}
	}
	
	@Test
	public void testWithPlainTexhyph() throws FileNotFoundException, UnsupportedEncodingException, TexParserException {
		Hyphenator hyph = new Hyphenator();
		File file = new File("target/generated-resources/hyph/hyph-fi.tex");
		FileInputStream fileStream = new FileInputStream(file);
		InputStreamReader streamReader = new InputStreamReader(fileStream, "UTF-8");
		hyph.loadTable(streamReader);
		
		for(String[] testCase : testCases) {
			assertEquals(testCase[0], testCase[1], hyph.hyphenate(testCase[2]));
		}
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
