# TURTLE4J API documentation

The private `jgs-lab/api-turtle4j` repository generates Javadoc. This repository hosts the generated HTML on GitHub Pages. The compiled library is available from Maven Central.

## Development documentation

In the private source repository, manually run [Update TURTLE4J Javadoc](https://github.com/jgs-lab/api-turtle4j/actions/workflows/update-javadoc.yml) from `main`. It opens a pull request in this repository with generated files under `docs/javadoc/development/`. Review and merge that pull request to publish the [development Javadoc](https://javiergs.github.io/turtle4j-getting-started/javadoc/development/).

## Versioned release documentation

Published release documentation under `docs/javadoc/<version>/` is immutable. The private [Generate TURTLE4J 1.0.0 Javadoc](https://github.com/jgs-lab/api-turtle4j/actions/workflows/generate-release-javadoc.yml) workflow requires the full commit SHA for the published release and refuses to overwrite the existing index. The [1.0.0 reference](https://javiergs.github.io/turtle4j-getting-started/javadoc/1.0.0/) is already published.

The Maven Central `-javadoc.jar` is a placeholder. Use the hosted reference for generated HTML.

## GitHub Pages

GitHub Pages deploys from the `/docs` folder on `main`.
