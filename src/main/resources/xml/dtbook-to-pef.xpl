<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step type="celia:dtbook-to-pef" version="1.0"
                xmlns:celia="http://www.celia.fi"
		xmlns:p="http://www.w3.org/ns/xproc"
		xmlns:px="http://www.daisy.org/ns/pipeline/xproc"
		xmlns:pef="http://www.daisy.org/ns/2008/pef"
		xmlns:c="http://www.w3.org/ns/xproc-step"
		xmlns:dtb="http://www.daisy.org/z3986/2005/dtbook/"
		exclude-inline-prefixes="#all"
		name="main">

    <p:documentation xmlns="http://www.w3.org/1999/xhtml">
        <h1 px:role="name">DTBook to PEF (Celia)</h1>
	<p px:role="desc">Transforms a DTBook (DAISY 3 XML) document into a PEF.</p>
    </p:documentation>

    <p:input port="source" primary="true" px:name="source" px:media-type="application/x-dtbook+xml"/>

    <p:option name="include-brf" select="'true'"/>
    <p:option name="include-preview" select="'true'"/>
    <p:option name="pef-output-dir"/>
    <p:option name="brf-output-dir"/>
    <p:option name="preview-output-dir"/>
    <p:option name="temp-dir"/>

    <p:option name="stylesheet" select="'http://www.celia.fi/pipeline/modules/braille/default.scss'"/>

    <p:option name="page-width" select="'27'"/>
    <p:option name="page-height" select="'30'"/>
    <p:option name="duplex" select="'true'"/>

    <p:option name="maximum-number-of-sheets" select="'80'"/>
    <p:option name="minimum-number-of-sheets" select="'0'"/>

    <p:option name="include-image-groups" select="'false'"/>
    <p:option name="include-images" select="'false'"/>
    <p:option name="include-captions" select="'false'"/>

    <p:option name="include-production-notes" select="'false'"/>

    <p:option name="include-note-references" select="'true'"/>
    <p:option name="process-note-references" px:type="boolean" select="'true'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Process noterefs</h2>
	    <p px:role="desc">When enabled, will insert square brackets around noteref elements.</p>
	</p:documentation>
    </p:option>

    <p:option name="include-notes" px:type="boolean" select="'true'"/>
    <p:option name="process-notes" px:type="boolean" select="'true'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Process notes</h2>
	    <p px:role="desc">When enabled, will insert the ID of the noteref and a colon before note elements.</p>
	</p:documentation>
    </p:option>
    
    <p:option name="show-braille-page-numbers" px:type="boolean" select="'true'"/>
    <p:option name="show-print-page-numbers" px:type="boolean" select="'false'"/>

    <p:option name="line-spacing" select="'single'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Line spacing</h2>
	    <p px:role="desc">Possible values: 'single' or 'double'.</p>
	</p:documentation>
    </p:option>
    
    <p:option name="letter-spacing" select="'0'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Letter spacing</h2>
	    <p px:role="desc">When set to 1, braille cells have empty spaces between them according to the Finnish braille specification. Furthermore, words are separated by 3 empty cells.</p>
	</p:documentation>
    </p:option>
    
    <p:option name="hyphenation" select="'true'"/>

    <p:option name="insert-titlepage" px:type="boolean" select="'true'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Insert title page</h2>
	    <p px:role="desc">When enabled, will insert the title page. The content of the title page is generated from the metadata of the book.</p>
	</p:documentation>
    </p:option>

    <p:option name="preprocess-tables" px:type="boolean" select="'false'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Preprocess tables</h2>
	    <p px:role="desc">When enabled, will translate the tables containing slashes differently to braille. The cells of the tables containing slashes will be separated by two dashes instead of slashes.</p>
	</p:documentation>
    </p:option>

    <p:option name="pad-volume-endings" px:type="boolean" select="'false'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Pad volume endings</h2>
	    <p px:role="desc">When enabled, will ensure that there is at least one empty braille sheet at the end of the volume to protect the physical print. Note: This may cause some volumes be up to two pages longer than what's set in Maximum number of sheets.</p>
	</p:documentation>
    </p:option>

    <p:option name="make-volumes-divisible-by-four" px:type="boolean" select="'false'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Make volumes divisible by four</h2>
	    <p px:role="desc">When enabled, will ensure that the number of pages in each volume is divisible by four, thus avoiding empty pages in the middle of the printed book. Works by appending empty pages at the end of each volumes. If Pad volume endings is set to true, then may cause some volumes to be slightly longer than what's set in Maximum number of sheets.</p>
	</p:documentation>
    </p:option>

    <p:option name="text-level-formatting" px:type="boolean" select="'true'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Text-level formatting</h2>
	    <p px:role="desc">When disabled, will skip text-level typography markers, such as em and strong.</p>
	</p:documentation>
    </p:option>

    <p:option name="toc-depth" select="'2'"/>
    <p:option name="include-volume-tocs" px:type="boolean" select="'true'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Include volume TOCs</h2>
	    <p px:role="desc">When enabled, includes volume-level tables of contents at the beginning of each volume.</p>
	</p:documentation>
    </p:option>
    <p:option name="include-document-toc-in-first-volume" px:type="boolean" select="'false'">
        <p:documentation xmlns="http://www.w3.org/1999/xhtml">
	    <h2 px:role="name">Include document TOC in first volume</h2>
	    <p px:role="desc">When enabled, includes document-level table of contents at the beginning of the first volume. Additionally, if both volume-level and document-level TOCs are enabled, then volume-level TOC is skipped in the first volume, because the information is redundant. The depth of the document-level table of contents is the same as that of volume-level TOCs.</p>
	</p:documentation>
    </p:option>


    <p:import href="http://www.daisy.org/pipeline/modules/braille/dtbook-to-pef/library.xpl"/>
    <p:import href="http://www.daisy.org/pipeline/modules/braille/xml-to-pef/library.xpl"/>
    <p:import href="http://www.daisy.org/pipeline/modules/file-utils/library.xpl"/>
    <p:import href="http://www.daisy.org/pipeline/modules/common-utils/library.xpl"/>
    <p:import href="http://www.celia.fi/pipeline/modules/braille/library.xpl"/>


    <p:in-scope-names name="in-scope-names"/>
    <px:merge-parameters>
        <p:input port="source">
            <p:pipe port="result" step="in-scope-names"/>
        </p:input>
    </px:merge-parameters>
    <px:delete-parameters parameter-names="stylesheet
                                           include-brf
                                           include-preview
                                           pef-output-dir
                                           brf-output-dir
                                           preview-output-dir
                                           temp-dir"/>

    <p:identity name="input-options"/>
    <p:sink/>

    <!-- =============== -->
    <!-- CREATE TEMP DIR -->
    <!-- =============== -->
    <px:tempdir name="temp-dir">
      <p:with-option name="href" select="if ($temp-dir!='') then $temp-dir else $pef-output-dir"/>
    </px:tempdir>
    <p:sink/>

    <!-- ==================== -->
    <!-- DTBOOK PREPROCESSING -->
    <!-- ==================== -->
    <p:identity>
      <p:input port="source">
        <p:pipe step="main" port="source"/>
      </p:input>
    </p:identity>

    <celia:pre-processing>
      <p:input port="parameters">
	<p:pipe step="input-options" port="result"/>
      </p:input>
      <p:with-option name="preprocess-tables" select="$preprocess-tables"/>
      <p:with-option name="insert-titlepage" select="$insert-titlepage"/>
    </celia:pre-processing>

    <!-- ============= -->
    <!-- DTBOOK TO PEF -->
    <!-- ============= -->
    <px:dtbook-to-pef.convert default-stylesheet="http://www.daisy.org/pipeline/modules/braille/dtbook-to-pef/css/default.css"
                              transform="(formatter:dotify)(translator:celia)">
	<p:with-option name="temp-dir" select="string(/c:result)">
	  <p:pipe step="temp-dir" port="result"/>
	</p:with-option>
	<p:with-option name="stylesheet" select="$stylesheet"/>
	<p:input port="parameters">
	  <p:pipe step="input-options" port="result"/>
	</p:input>
    </px:dtbook-to-pef.convert>

    <!-- ================== -->
    <!-- PEF POSTPROCESSING -->
    <!-- ================== -->

    <celia:post-processing>
      <p:input port="parameters">
        <p:pipe step="input-options" port="result"/>
      </p:input>
      <p:with-option name="make-volumes-divisible-by-four" select="$make-volumes-divisible-by-four"/>
      <p:with-option name="pad-volume-endings" select="$pad-volume-endings"/>
      <p:with-option name="duplex" select="$duplex"/>
    </celia:post-processing>

    <!-- ========= -->
    <!-- STORE PEF -->
    <!-- ========= -->
    <px:xml-to-pef.store>
      <p:input port="obfl">
        <p:empty/>
      </p:input>
      <p:with-option name="name" select="replace(p:base-uri(/),'^.*/([^/]*)\.[^/\.]*$','$1')">
        <p:pipe step="main" port="source"/>
      </p:with-option>
      <p:with-option name="include-brf" select="$include-brf"/>
      <p:with-option name="include-preview" select="$include-preview"/>
      <p:with-option name="pef-output-dir" select="$pef-output-dir"/>
      <p:with-option name="brf-output-dir" select="$brf-output-dir"/>
      <p:with-option name="preview-output-dir" select="$preview-output-dir"/>
    </px:xml-to-pef.store>

</p:declare-step>
