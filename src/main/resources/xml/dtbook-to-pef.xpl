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

    <p:option name="transform" px:data-type="transform-query"/>

    <p:option name="page-width" select="'27'"/>
    <p:option name="page-height" select="'30'"/>
    <p:option name="duplex" select="'true'"/>

    <p:option name="maximum-number-of-sheets" select="'80'"/>
    <p:option name="minimum-number-of-sheets" select="'0'"/>

    <p:option name="include-image-groups" select="'false'"/>
    <p:option name="include-images" select="'false'"/>
    <p:option name="include-captions" select="'false'"/>

    <p:option name="include-production-notes" select="'false'"/>
    <p:option name="process-noterefs" select="'true'"/>
    <p:option name="process-notes" select="'true'"/>

    <p:option name="show-braille-page-numbers" select="'true'"/>
    <p:option name="show-print-page-numbers" select="'false'"/>

    <p:option name="line-spacing" select="'0'"/>
    <p:option name="letter-spacing" select="'0'"/>
    <p:option name="hyphenation" select="'true'"/>

    <p:option name="insert-titlepage" select="'true'"/>

    <p:option name="preprocess-tables" select="'false'"/>

    <p:option name="make-volumes-divisible-by-four" select="'false'"/>

    <p:option name="skip-typography" select="'false'"/>

    <p:option name="toc-depth" select="'2'"/>


    <p:import href="http://www.daisy.org/pipeline/modules/braille/dtbook-to-pef/library.xpl"/>
    <p:import href="http://www.daisy.org/pipeline/modules/braille/pef-utils/library.xpl"/>
    <p:import href="http://www.daisy.org/pipeline/modules/file-utils/library.xpl"/>
    <p:import href="http://www.daisy.org/pipeline/modules/common-utils/library.xpl"/>
    <p:import href="http://www.celia.fi/pipeline/modules/braille/library.xpl"/>


    <p:in-scope-names name="in-scope-names"/>
    <p:identity>
      <p:input port="source">
        <p:pipe port="result" step="in-scope-names"/>
      </p:input>
    </p:identity>
    <p:delete match="c:param[@name=('stylesheet',
                                    'ascii-table',
				    'include-brf',
				    'include-preview',
				    'pef-output-dir',
				    'brf-output-dir',
				    'preview-output-dir',
				    'temp-dir')]"/>
    <p:add-attribute match="c:param[@name='hyphenation']" attribute-name="value">
        <p:with-option name="attribute-value"
	             select="if ($hyphenation='from-meta')
		             then (//dtb:meta[@name='prod:docHyphenate']/@content,'Y')[1]='Y'
			     else $hyphenation='true'"/>
    </p:add-attribute>

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
    </celia:post-processing>

    <!-- ========= -->
    <!-- STORE PEF -->
    <!-- ========= -->
    <p:group>
      <p:variable name="name" select="replace(p:base-uri(/),'^.*/([^/]*)\.[^/\.]*$','$1')">
        <p:pipe step="main" port="source"/>
      </p:variable>
      <pef:store>
                  <p:with-option name="href" select="concat($pef-output-dir,'/',$name,'.pef')"/>
                  <p:with-option name="preview-href" select="if ($include-preview='true' and $preview-output-dir!='')
                                                             then concat($preview-output-dir,'/',$name,'.pef.html')
                                                             else ''"/>
                  <p:with-option name="brf-href" select="if ($include-brf='true' and $brf-output-dir!='')
                                                         then concat($brf-output-dir,'/',$name,'.brf')
                                                         else ''"/>
              </pef:store>
    </p:group>

</p:declare-step>
