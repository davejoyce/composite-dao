composite-dao
=============

Demonstrates the [Composite DAO pattern](http://davejoyce.github.io/projects/2013/04/24/composite-dao-pattern) using the Spring Framework.

What does it do?
----------------

This demo app uses the [Spring Social Google](https://github.com/GabiAxel/spring-social-google) extension and a small application-local RDBMS to show how a composite DAO works in a _local vs. remote_ scenario.

* Performs RESTful interactions with the [Google Tasks API](https://developers.google.com/google-apps/tasks/) for remote operations
* Performs CRUD operations on the RDBMS via JPA / [Hibernate](http://www.hibernate.org/)

Continuous integration
----------------------

[![Build Status](https://buildhive.cloudbees.com/job/davejoyce/job/composite-dao/badge/icon)](https://buildhive.cloudbees.com/job/davejoyce/job/composite-dao/)

Continuous integration for this project is provided by CloudBees [BuildHive](http://buildhive.cloudbees.com/).

License
-------

This project is licensed under the [Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
