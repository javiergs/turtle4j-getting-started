# TURTLE4J API documentation

Generated Javadoc is hosted in this repository, separately from the compiled library published on Maven Central.

## Development documentation

The [Update TURTLE4J Javadoc](../../.github/workflows/update-javadoc.yml) GitHub Actions workflow runs daily and can be triggered manually from the **Actions** tab. It checks out `jgs-lab/api-turtle4j` at `main`, builds the HTML Javadoc using Java 21 and Maven, and opens a pull request updating `docs/javadoc/development/`. Review and merge that pull request to publish the changes; no manual generation script is needed.

## Versioned release documentation

Published release documentation is kept under `docs/javadoc/<version>/` and must remain immutable. In particular, `docs/javadoc/1.0.0/` will hold the Javadoc for the published **1.0.0** release. The automatic development workflow **does not generate or update this directory**. Its initial HTML content still needs to be generated from the exact 1.0.0 source revision and committed separately.

The Maven Central `-javadoc.jar` is intentionally a placeholder and does not contain generated HTML.

## GitHub Pages

Once generated HTML has been committed, enable GitHub Pages with **Deploy from a branch**, branch `main`, folder `/docs`. The versioned Javadoc will then be served at `/turtle4j-getting-started/javadoc/1.0.0/` and development Javadoc at `/turtle4j-getting-started/javadoc/development/`.
