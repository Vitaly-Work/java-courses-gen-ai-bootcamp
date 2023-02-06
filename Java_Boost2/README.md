# Java Boost #2
<img src="materials/images/java_boost_2_100.png" alt="Java Boost 2 logo">

<br>
A place where we are developing a set of the JAT training modules(includes questions and hands-on tasks).

We are trying to keep materials from this repo as much universal as possible. This approach let use this materials in multiple trainings and mentoring.

This repository is created to be open for any change proposed by anyone in open source way - so any contribution is appreciated.

## Contribution Rules

### Content contributors:
- Anyone can propose a change to this repository via Merge Request.
- Only CODEOWNERS members can merge to master.
- If you add a new service/core feature consider to add a question about it to a control question section. Use common sense.

### Coordinators:
- Hot-fixes should be merged to master.
- Use 'release' branch as a source for active training run. Do not commit to this branch! Cherry-pick commits from master branch to release one if there are any hot-fixes.
- There could be more than one "release" branch. For instance in case of some differences in scope\date of the active trainings it could be created several branches like: `release_Java_Boost_For_Developer_010123`, `release_Java_Boost_Developer_mentoring_2`. Creator of the branch is responsible for maintaining it in actual state. Use "Cherry-pick  for hot-fixes" rule for this branches.
- Release branch should be never merged to a master branch. Just delete it when it get stale and create a new one from the actual commit in a master.
- Tag the release commit with the incremental version.
- We use `ema.ms` service as an alias for course modules. This adds elasticity for GIT branching strategy.
  There is only one owner for links, contact Coordinators to make edits.

## Courses

- [Java Boost #2](courses/Java_Advanced_Training/README.md)

## Modules

- [Java core](courses/Java_Advanced_Training/core/README.md)
- [Java features](courses/Java_Advanced_Training/features/README.md)
- [XML](courses/Java_Advanced_Training/xml/README.md)
- [SQL](courses/Java_Advanced_Training/sql/README.md)
- [WEB](courses/Java_Advanced_Training/web/README.md)
- [WEB-services](courses/Java_Advanced_Training/web_service/README.md)
- [Design patterns](courses/Java_Advanced_Training/design_pattern/README.md)
- [Spring](courses/Java_Advanced_Training/spring/README.md)
- [Hibernate](courses/Java_Advanced_Training/hibernate/README.md)
- [JMS](courses/Java_Advanced_Training/jms/README.md)
- [Testing](courses/Java_Advanced_Training/testing/README.md)
- [Microservices](courses/Java_Advanced_Training/ms/README.md)
- [Cloud](courses/Java_Advanced_Training/cloud/README.md)
- [Software development methodologies](courses/Java_Advanced_Training/methodology/README.md)
- [Advanced modules](courses/Java_Advanced_Training/advanced/README.md)

## Tasks
There are some optional tasks for modules. 