# Relational database for developers
<img src="materials/images/Sql_data_base_with_logo.png" alt="sql logo" width="256">

<br>
A place where we are developing a set of the SQL training modules(includes self-study materials and hands-on tasks).

We are trying to keep materials from this repo as much universal as possible. This approach let use this materials in multiple trainings and mentoring.

This repository is created to be open for any change proposed by anyone in open source way - so any contribution is appreciated.

## Contribution Rules

### Content contributors:
- Anyone can propose a change to this repository via Merge Request.
- Only CODEOWNERS members can merge to master.
- If you add a new feature consider to add a question about it to a control question section. Use common sense.

### Coordinators:
- Hot-fixes should be merged to master.
- Use 'release' branch as a source for active training run. Do not commit to this branch! Cherry-pick commits from master branch to release one if there are any hot-fixes.
- There could be more then one "release" branch. For instance in case of some differences in scope\date of the active trainings it could be created several branches like: `release_SQL_For_Developer_010123`, `release_SQL_For_Developer_mentoring_2`. Creator of the branch is responsible for maintaining it in actual state. Use "Cherry-pick  for hot-fixes" rule for this branches.
- Release branch should be never merged to a master branch. Just delete it when it get stale and create a new one from the actual commit in a master.
- Tag the release commit with the incremental version.
- We use `ema.ms` service as an alias for course modules. This adds elasticity for GIT branching strategy.
  There is only one owner for links, contact people below to make edits. If people below are unreachable, you can contact Support to change owner: (TODO add later)

## Courses

- [Relational database for developers](materials/README.md)

## Modules
- [General Questions on SQL](materials/general_overview/general_overview_content.md)
- [Data Definition Statements](materials/ddl/ddl_content.md)
- [Data Manipulation Statements](materials/dml/dml_content.md)
- [Transactional and Locking Statements](materials/tcl/tcl_content.md)
- [Functions and Operator References](materials/functions/functions_content.md)
- [Performance tuning](materials/performance_tuning/performance_content.md)


## Quizes
There are quizes for the following modules on examinator.epam.com:

- Database Services - xx questions

You can integrate them to a learning path with help of L&D specialists.
Please contact @Kseniya Yudzichava in case if you would like to request Quiz assignement for your mentees or you would like to contribute to Quizes development.