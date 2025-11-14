This project is largely influenced by Portal Runner's video "Renaming This File Changes what it Does"

It works by editing the header of the ICO file and appending the PDF file afterwards.

Most file type headers have 2 main conditions.

1. The header must be the first thing in the file
2. The data must come directly after the header

ICO files and PDF files break both of these rules allowing for this kind of embedding.

The ICO data can be anywhere provided that the offset specified in the header points to the data locations.

PDF files can have their header anywhere in the first 1k bytes.

This way we can add the pdf header and data directly after the ICO header (22 Bytes) and then change the ICO data offset to be the size of the PDF file in bytes + 22.

That way when the file extension is ICO it reads the header and jumps passed the PDF data to the ICO data, and when the file extention is PDF it ignores the ICO header and looks for the PDF header in the first 1k bytes.
