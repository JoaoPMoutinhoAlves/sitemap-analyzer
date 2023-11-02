# sitemap-analyzer

## How to use
Go to [Releases](https://github.com/JoaoPMoutinhoAlves/sitemap-analyzer/releases) and download the latest .ZIP file. Unzip it and edit the file `application.properties` the properties as required. Save and then execute (double click) the executable (.exe) file.

## Properties definitions
- `sitemap.url`: URL of the sitemap to analyze. It must be a valid URL.
- `sitemap.url.extension`: Extension of the sitemap URL's to analyze. It should either be 'html' or empty as required.
- `pagespeed.categories`: Can be one or more of the following: `PERFORMANCE,ACCESSIBILITY,BEST_PRACTICES,SEO,PWA`. If more than one, they must be comma separated.
- `pagespeed.strategy`: Can be either `MOBILE` or `DESKTOP`. If left empty, it will default to `DESKTOP`.
- `google.api.key`: API key to use for the Google PageSpeed API.
- `output.directory`: Directory where the output files will be stored.
