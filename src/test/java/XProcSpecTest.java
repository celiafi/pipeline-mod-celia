import javax.inject.Inject;

import java.io.File;

import com.google.common.collect.ImmutableMap;

import org.daisy.maven.xproc.xprocspec.XProcSpecRunner;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.PathUtils;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class XProcSpecTest {
	
	@Inject
	private XProcSpecRunner xprocspecRunner;
	
	@Test
	public void runXProcSpec() throws Exception {
		File baseDir = new File(PathUtils.getBaseDir());
		boolean success = xprocspecRunner.run(ImmutableMap.of(
			                                      "test_translator",
			                                      new File(baseDir, "src/test/xprocspec/test_translator.xprocspec"),
			                                      "test_css_formatter",
			                                      new File(baseDir, "src/test/xprocspec/test_css_formatter.xprocspec"),
			                                      "test_script",
			                                      new File(baseDir, "src/test/xprocspec/test_script.xprocspec")
			                                      ),
		                                      new File(baseDir, "target/xprocspec-reports"),
		                                      new File(baseDir, "target/surefire-reports"),
		                                      new File(baseDir, "target/xprocspec"),
		                                      null,
		                                      new XProcSpecRunner.Reporter.DefaultReporter());
		assertTrue("XProcSpec tests should run with success", success);
	}
	
	@Configuration
	public Option[] config() {
		return Config.config();
	}
}
