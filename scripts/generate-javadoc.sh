#!/usr/bin/env bash
set -euo pipefail

# Run from the getting-started repository root.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
VERSION="1.0.0"
SOURCE_REPO="${1:-../api-turtle4j}"
SOURCE_REPO="$(cd "$SOURCE_REPO" && pwd)"

if [[ ! -f "$SOURCE_REPO/pom.xml" ]]; then
  echo "Expected TURTLE4J source repository at: $SOURCE_REPO" >&2
  echo "Usage: bash scripts/generate-javadoc.sh /path/to/api-turtle4j" >&2
  exit 1
fi

# Generate documentation from the source tree, not the placeholder Maven Central Javadoc JAR.
mvn -f "$SOURCE_REPO/pom.xml" -DskipTests javadoc:javadoc

GENERATED="$SOURCE_REPO/target/site/apidocs"
if [[ ! -f "$GENERATED/index.html" ]]; then
  echo "Javadoc generation did not produce index.html" >&2
  exit 1
fi

DEST="$ROOT/docs/javadoc/$VERSION"
mkdir -p "$DEST"
cp -R "$GENERATED/." "$DEST/"
echo "Generated TURTLE4J $VERSION Javadoc in $DEST"
echo "Commit the generated files through a pull request."
