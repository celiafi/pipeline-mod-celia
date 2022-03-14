#!/usr/bin/perl
use File::Copy;
use File::Path qw( make_path );

use IO::Compress::Zip qw(:all);

my $dir_qfn = './target/celia-hyphenation-tables';
make_path($dir_qfn);

copy("./target/generated-resources/hyph/hyph-fi.tex","./target/celia-hyphenation-tables/hyph-fi.tex") or die "Copy failed: $!";
copy("./target/generated-resources/hyph/hyph-fi.dic","./target/celia-hyphenation-tables/hyph-fi.dic") or die "Copy failed: $!";
zip [ glob("./target/celia-hyphenation-tables/*") ] => "./target/celia-hyphenation-tables.zip" or die "Cannot create zip file: $ZipError";

use File::Path qw( rmtree );
rmtree("./target/celia-hyphenation-tables") or die "Cannot rmtree './target/celia-hyphenation-tables' : $!";
