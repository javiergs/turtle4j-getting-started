# Contributing to TURTLE4J Getting Started

Thank you for helping improve the public examples, tutorials, and documentation for **TURTLE4J**.

This repository is a **public learning and examples project**. The underlying TURTLE4J library source is maintained separately in a **private repository**. You do **not** need access to that source to contribute here: use the published `com.javiergs:turtle4j:1.1.0` Maven dependency.

## What to contribute

Contributions are welcome for:

- Clearer installation and simulator setup instructions.
- Reproducible Java examples using the published API.
- Tests and bug fixes for examples.
- Tutorials that explain ROS 2 topics, message types, subscriptions, and publishing.
- Documentation of observed limitations and troubleshooting steps.

For research prototypes and robot-specific applications developed by the ROVER team, use the team's [ASU26-ROVER repository](https://github.com/calhci/ASU26-ROVER) and its accepted-story/task workflow. Propose an example here only when it is sufficiently general, documented, and reproducible for other learners.

**Do not submit changes to the private TURTLE4J library implementation here.** If an example reveals a missing library capability, explain the use case and expected behavior in an issue or PR discussion; the maintainers will evaluate potential API changes separately.

## Propose a change

You do not need write access to this repository.

1. **Fork** `javiergs/turtle4j-getting-started` to your GitHub account.
2. Create a descriptively named branch in your fork (for example, `docs/simulator-troubleshooting` or `examples/pose-monitor`).
3. Make one focused change. Keep the published library version unchanged unless a maintainer explicitly requests an update.
4. Add or update documentation describing how to reproduce the behavior.
5. Run `mvn clean verify`; for simulator-dependent examples, run them against Turtlesim and record what you observed.
6. Push your branch and open a **pull request against this repository's `main` branch**.

## Pull request checklist

Include the following in your PR description:

- **Purpose:** What problem does this change solve?
- **Changes:** Which examples or documentation were added or updated?
- **How to test:** Exact commands and prerequisites.
- **Results:** Actual observed output, screenshots, or logs when relevant.
- **ROS details:** Topic names, message types, and sample payloads for ROS examples.
- **Limitations and safety:** What is specific to Turtlesim; what has *not* been validated on hardware.
- **Related work:** Link a ROVER issue or PR when the contribution originated there.

Keep examples small, readable, and focused on the public API. Avoid credentials, personal data, hard-coded private network addresses, generated build artifacts, and unrelated formatting changes.

## Review and merging

All changes to `main` must go through a pull request. The repository uses branch protection, CI, and code-owner review. **A maintainer must approve a contribution before it is merged.** Contributors should respond to review comments and update their PR as needed; additional commits may require a new approval.

Opening a PR does not guarantee acceptance. Do not merge or push directly to `main`.

## Reporting issues

For a bug or documentation problem, open a GitHub issue with the environment (OS, Java version, Docker version), reproduction steps, expected result, actual result, and relevant logs. Avoid posting secrets or private robot connection details.
