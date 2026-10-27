# TURTLE4J API documentation

Versioned generated API documentation lives in `docs/javadoc/<version>/`.

For **1.0.0**, run from this repository root:

```bash
bash scripts/generate-javadoc.sh ../api-turtle4j
```

The command requires a local checkout of the TURTLE4J library source repository and Maven. It generates HTML from the Java source using the Javadoc Maven plugin, then copies the output to `docs/javadoc/1.0.0/`.

**The `-javadoc.jar` on Maven Central is intentionally a placeholder** and cannot be used to produce the HTML documentation.

Commit the generated `docs/javadoc/1.0.0/` files in a follow-up PR. Enable GitHub Pages with **Deploy from a branch**, branch `main`, folder `/docs`, after merging the generated documentation. The public path will then be `/turtle4j-getting-started/javadoc/1.0.0/`.

Do not overwrite the 1.0.0 documentation when publishing future versions.
