import javax.inject.Inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;

import net.davidashen.text.Hyphenator;
import net.davidashen.text.Utf8TexParser.TexParserException;

import static org.daisy.pipeline.braille.common.Query.util.query;
import org.daisy.pipeline.braille.libhyphen.LibhyphenHyphenator;
import org.daisy.pipeline.braille.tex.TexHyphenator;

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
		testCases.add(new String[]{"Longer vowel strings", "ai\u00ADe tau\u00ADois\u00ADsa", "aie tauoissa"});
		testCases.add(new String[]{"Word-initial single vowels", "alas eteen iso oli uros yhä äly öky", "alas eteen iso oli uros yhä äly öky"});
		testCases.add(new String[]{"Vowel triplets and compound words", "ka\u00ADvi\u00ADaa\u00ADri ra\u00ADdi\u00ADo\u00ADuu\u00ADti\u00ADset vir\u00ADhe\u00ADuu\u00ADti\u00ADnen maa\u00ADot\u00ADte\u00ADlu ve\u00ADto\u00ADuis\u00ADtin sää\u00ADil\u00ADmi\u00ADö suo\u00ADa\u00ADlu\u00ADe uraa\u00ADuur\u00ADta\u00ADva puu\u00ADis\u00ADtu\u00ADtus ka\u00ADla\u00ADuu\u00ADni ki\u00ADvi\u00ADuu\u00ADni va\u00ADle\u00ADaa\u00ADri\u00ADa pii\u00ADotus maa\u00ADui\u00ADma\u00ADla", "kaviaari radiouutiset virheuutinen maaottelu vetouistin sääilmiö suoalue uraauurtava puuistutus kalauuni kiviuuni valeaaria piiotus maauimala"});
		testCases.add(new String[]{"Compound word with hyphen", "rek\u00ADka-\u200Bau\u00ADto", "rekka-auto"});
		testCases.add(new String[]{"Native consonant clusters", "kars\u00ADta\u00ADvers\u00ADtas", "karstaverstas"});
		testCases.add(new String[]{"Borrowed consonant clusters", "eks\u00ADpres\u00ADsii\u00ADvi\u00ADnen verk\u00ADko\u00ADstra\u00ADte\u00ADgi\u00ADa yö\u00ADklu\u00ADbil\u00ADle ar\u00ADtik\u00ADla klo\u00ADset\u00ADti klii\u00ADmak\u00ADsi ak\u00ADkre\u00ADdi\u00ADtoin\u00ADti ag\u00ADgres\u00ADsi\u00ADo", "ekspressiivinen verkkostrategia yöklubille artikla klosetti kliimaksi akkreditointi aggressio"});
		testCases.add(new String[]{"Non-word-ending and word-ending s", "os\u00ADtos", "ostos"});
		testCases.add(new String[]{"Exemplary written standard Finnish. Electronic archives of Institute for the Languages of Finland.",
		"Kan\u00ADsa\u00ADlai\u00ADset. Vuo\u00ADsi sit\u00ADten ku\u00ADvai\u00ADlin al\u00ADka\u00ADvan 1980-\u200Blu\u00ADvun kan\u00ADsain\u00ADvä\u00ADli\u00ADsi\u00ADä nä\u00ADky\u00ADmi\u00ADä mus\u00ADtan\u00ADpu\u00ADhu\u00ADvin vä\u00ADrein. Lien\u00ADny\u00ADtys\u00ADke\u00ADhi\u00ADtys oli py\u00ADsäh\u00ADty\u00ADnyt ja suur\u00ADval\u00ADto\u00ADjen suh\u00ADteet oli\u00ADvat puo\u00ADles\u00ADsa vuo\u00ADdes\u00ADsa Wie\u00ADnin huip\u00ADpu\u00ADko\u00ADkouk\u00ADsen jäl\u00ADkeen kär\u00ADjis\u00ADty\u00ADneet. Myös\u00ADkään päät\u00ADty\u00ADneen vuo\u00ADden ke\u00ADhi\u00ADtys ei va\u00ADli\u00ADtet\u00ADta\u00ADvas\u00ADti an\u00ADna ai\u00ADhet\u00ADta va\u00ADloi\u00ADsam\u00ADpaan ar\u00ADvi\u00ADoon. Kan\u00ADsain\u00ADvä\u00ADlis\u00ADtä ti\u00ADlan\u00ADnet\u00ADta lei\u00ADmaa ylei\u00ADnen epä\u00ADvar\u00ADmuus se\u00ADkä huo\u00ADli huo\u00ADmi\u00ADses\u00ADta. Suur\u00ADval\u00ADto\u00ADjen kes\u00ADki\u00ADnäi\u00ADnen epä\u00ADluu\u00ADloi\u00ADsuus ja ky\u00ADräi\u00ADly näyt\u00ADtä\u00ADvät jat\u00ADku\u00ADvan sa\u00ADmal\u00ADla kun ase\u00ADva\u00ADrus\u00ADte\u00ADlu ete\u00ADnee kiih\u00ADty\u00ADväs\u00ADsä tah\u00ADdis\u00ADsa. Täl\u00ADlai\u00ADnen ti\u00ADlan\u00ADne ei tie\u00ADten\u00ADkään ole mi\u00ADkään uu\u00ADsi ja en\u00ADnen\u00ADko\u00ADke\u00ADma\u00ADton. Tä\u00ADnään sen te\u00ADkee kui\u00ADten\u00ADkin va\u00ADka\u00ADvak\u00ADsi ase\u00ADtek\u00ADni\u00ADnen ke\u00ADhi\u00ADtys, jon\u00ADka mit\u00ADta\u00ADsuh\u00ADtei\u00ADta ny\u00ADky\u00ADih\u00ADmi\u00ADsen näyt\u00ADtää ole\u00ADvan vai\u00ADke\u00ADa täy\u00ADsin ym\u00ADmär\u00ADtää. Vaik\u00ADka suur\u00ADso\u00ADdal\u00ADta on\u00ADkin kol\u00ADmen ja puo\u00ADlen vuo\u00ADsi\u00ADkym\u00ADme\u00ADnen ai\u00ADka\u00ADna väl\u00ADtyt\u00ADty, suur\u00ADval\u00ADta\u00ADsuh\u00ADteet ovat use\u00ADam\u00ADman\u00ADkin ker\u00ADran kär\u00ADjis\u00ADty\u00ADneet ja kan\u00ADsain\u00ADvä\u00ADli\u00ADnen jän\u00ADni\u00ADtys on pur\u00ADkau\u00ADtu\u00ADnut yli sa\u00ADdak\u00ADsi alu\u00ADeel\u00ADli\u00ADsek\u00ADsi selk\u00ADkauk\u00ADsek\u00ADsi. Esi\u00ADmer\u00ADkik\u00ADsi vii\u00ADme vuo\u00ADsi\u00ADkym\u00ADme\u00ADnel\u00ADlä ei ol\u00ADlut päi\u00ADvää\u00ADkään, jol\u00ADloin jos\u00ADsain osas\u00ADsa maa\u00ADil\u00ADmaa ei oli\u00ADsi käy\u00ADty so\u00ADtaa. Kun näyt\u00ADtä\u00ADmö\u00ADnä ovat useim\u00ADmi\u00ADten ol\u00ADleet Suo\u00ADmes\u00ADta etääl\u00ADlä si\u00ADjait\u00ADse\u00ADvat alu\u00ADeet, mei\u00ADdän on ol\u00ADlut help\u00ADpo ot\u00ADtaa oma\u00ADhy\u00ADväi\u00ADnen si\u00ADvus\u00ADta\u00ADkat\u00ADso\u00ADjan asen\u00ADne ja ku\u00ADvi\u00ADtel\u00ADla ole\u00ADvam\u00ADme tur\u00ADvas\u00ADsa. Pel\u00ADkään\u00ADpä, et\u00ADtä täl\u00ADlai\u00ADseen ajat\u00ADte\u00ADluun tuu\u00ADdit\u00ADtau\u00ADtu\u00ADmi\u00ADnen saat\u00ADtaa kui\u00ADten\u00ADkin ol\u00ADla pa\u00ADhan\u00ADlais\u00ADta it\u00ADse\u00ADpe\u00ADtos\u00ADta.",
		"Kansalaiset. Vuosi sitten kuvailin alkavan 1980-luvun kansainvälisiä näkymiä mustanpuhuvin värein. Liennytyskehitys oli pysähtynyt ja suurvaltojen suhteet olivat puolessa vuodessa Wienin huippukokouksen jälkeen kärjistyneet. Myöskään päättyneen vuoden kehitys ei valitettavasti anna aihetta valoisampaan arvioon. Kansainvälistä tilannetta leimaa yleinen epävarmuus sekä huoli huomisesta. Suurvaltojen keskinäinen epäluuloisuus ja kyräily näyttävät jatkuvan samalla kun asevarustelu etenee kiihtyvässä tahdissa. Tällainen tilanne ei tietenkään ole mikään uusi ja ennenkokematon. Tänään sen tekee kuitenkin vakavaksi asetekninen kehitys, jonka mittasuhteita nykyihmisen näyttää olevan vaikea täysin ymmärtää. Vaikka suursodalta onkin kolmen ja puolen vuosikymmenen aikana vältytty, suurvaltasuhteet ovat useammankin kerran kärjistyneet ja kansainvälinen jännitys on purkautunut yli sadaksi alueelliseksi selkkaukseksi. Esimerkiksi viime vuosikymmenellä ei ollut päivääkään, jolloin jossain osassa maailmaa ei olisi käyty sotaa. Kun näyttämönä ovat useimmiten olleet Suomesta etäällä sijaitsevat alueet, meidän on ollut helppo ottaa omahyväinen sivustakatsojan asenne ja kuvitella olevamme turvassa. Pelkäänpä, että tällaiseen ajatteluun tuudittautuminen saattaa kuitenkin olla pahanlaista itsepetosta."});
	}
	
	@Inject
	BundleContext context;
	
	@Test
	public void testWithLibhyphen() {
		LibhyphenHyphenator hyphenator
			= ((LibhyphenHyphenator.Provider)context.getService(context.getServiceReference(LibhyphenHyphenator.Provider.class.getName())))
			.get(query("(table:'hyph-fi.dic')")).iterator().next();
		for(String[] testCase : testCases) {
			assertEquals(testCase[0], testCase[1], hyphenator.asFullHyphenator().transform(new String[]{testCase[2]})[0]);
		}
	}
	
	@Inject
	TexHyphenator.Provider texhyphProvider;
	
	@Test
	public void testWithTexhyph() {
		for(String[] testCase : testCases) {
			assertEquals(testCase[0], testCase[1],
			             texhyphProvider.get(query("(table:'hyph-fi.properties')")).iterator().next()
			                            .asFullHyphenator()
			                            .transform(new String[]{testCase[2]})[0]);
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
		return Config.config();
	}
}
