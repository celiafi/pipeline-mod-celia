<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step type="celia:dtbook-to-pef" version="1.0"
                xmlns:celia="http://www.celia.fi"
		xmlns:p="http://www.w3.org/ns/xproc"
		xmlns:px="http://www.daisy.org/ns/pipeline/xproc"
		exclude-inline-prefixes="#all"
		name="main">

    <p:documentation xmlns="http://www.w3.org/1999/xhtml">
        <h1 px:role="name">DTBook to PEF (Celia)</h1>
	<p px:role="desc">Transforms a DTBook (DAISY 3 XML) document into a PEF.</p>
    </p:documentation>

    <p:input port="source" primary="true" px:name="source" px:media-type="application/x-dtbook+xml"/>

    <p:option name="include-brf"/>
    <p:option name="pef-output-dir"/>
    <p:option name="brf-output-dir"/>
    <p:option name="temp-dir"/>

    <p:option name="stylesheet" select="'http://www.celia.fi/pipeline/modules/braille/default.scss'"/>

    <p:option name="transform" px:data-type="transform-query"/>

    <p:option name="page-width" select="'27'"/>
    <p:option name="page-height" select="'30'"/>
    <p:option name="duplex"/>

    <p:option name="include-captions"/>
    <p:option name="include-images"/>
    <p:option name="include-image-groups"/>

    <p:option name="include-note-references"/>
    <p:option name="include-production-notes"/>

    <p:option name="show-braille-page-numbers"/>
    <p:option name="show-print-page-numbers"/>

    <p:option name="line-spacing" select="'0'"/>
    <p:option name="hyphenation"/>

    <p:option name="insert-titlepage" select="'true'"/>

    <p:option name="toc-depth" select="'2'"/>

    <p:import href="http://www.daisy.org/pipeline/modules/common-utils/library.xpl"/>
    <p:import href="http://www.daisy.org/pipeline/modules/braille/dtbook-to-pef/dtbook-to-pef.xpl"/>
    <p:import href="http://www.celia.fi/pipeline/modules/braille/library.xpl"/>

    <p:choose>
        <p:when test="$insert-titlepage='true'">
            <p:in-scope-names name="parameters"/>
            <celia:pre-processing>
                <p:input port="parameters">
                    <p:pipe port="result" step="parameters"/>
                </p:input>
                <p:input port="source">
                    <p:pipe port="source" step="main"/>
                </p:input>
            </celia:pre-processing>
        </p:when>
        <p:otherwise>
            <px:message message="Skipping title page generation"/>
        </p:otherwise>
    </p:choose>

    <px:dtbook-to-pef>
        <p:with-option name="transform" select="'(translator:celia)'"/>
	<p:with-option name="include-brf" select="$include-brf"/>
	<p:with-option name="pef-output-dir" select="$pef-output-dir"/>
	<p:with-option name="brf-output-dir" select="$brf-output-dir"/>
	<p:with-option name="stylesheet" select="$stylesheet"/>

	<p:with-option name="page-width" select="$page-width"/>
	<p:with-option name="page-height" select="$page-height"/>

	<p:with-option name="duplex" select="$duplex"/>

	<p:with-option name="include-captions" select="$include-captions"/>
	<p:with-option name="include-images" select="$include-images"/>
	<p:with-option name="include-image-groups" select="$include-image-groups"/>

	<p:with-option name="include-note-references" select="$include-note-references"/>
	<p:with-option name="include-production-notes" select="$include-production-notes"/>

	<p:with-option name="show-braille-page-numbers" select="$show-braille-page-numbers"/>
	<p:with-option name="show-print-page-numbers" select="$show-print-page-numbers"/>

	<p:with-option name="line-spacing" select="$line-spacing"/>
	<p:with-option name="hyphenation" select="$hyphenation"/>

	<p:with-option name="toc-depth" select="$toc-depth"/>
    </px:dtbook-to-pef>

</p:declare-step>
