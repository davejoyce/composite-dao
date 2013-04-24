composite-dao
=============

Demonstrates combination of Composite and DAO patterns using the Spring Framework.

[![Build Status](https://buildhive.cloudbees.com/job/davejoyce/job/composite-dao/badge/icon)](https://buildhive.cloudbees.com/job/davejoyce/job/composite-dao/)

What does this demo show?
-------------------------

This demo app uses the [Spring Social Google](https://github.com/guznik/spring-social-google)
extension and a small application-local RDBMS to show how a composite DAO works in the
_Local vs. Remote_ scenario described below. This demo app:

* Performs RESTful interactions with the [Google Tasks API](https://developers.google.com/google-apps/tasks/) for remote operations
* Performs CRUD operations on the RDBMS via JPA / [Hibernate](http://www.hibernate.org/)

What's the point?
-----------------

A composite DAO object enables transparent manipulation of persistent entities
that may reside in multiple storage repositories. By wrapping source-specific DAOs in
a composite DAO, a dependent service can perform normal, simple interaction with a DAO instance
(the composite DAO) and **without** having to contain any source-specific logic.

Within the composite DAO, each encapsulated, source-specific DAO is responsible for all the
nitty-gritty particulars of interaction with its source. Over that, the composite DAO contains
simple logic such as:

1. Check component DAO _A_. If result is non-null, return it. Otherwise,
2. Check component DAO _B_.

When would I use this?
----------------------

Here are a couple of example scenarios where composite DAO would benefit an application. These are
only examples - your experience and context may expose more. In other words, YMMV!

1. __Local vs. Remote:__ the Person resource officially resides in a 3rd-party system outside the
  organization, and it is cached within the organization in an RDBMS. In this scenario, the
  `PersonCompositeDAO` object contains a reference to
    1. a JPA/Hibernate `PersonDAO` implementation (the internal RDBMS)
    2. a SOAP-based WS `PersonDAO` implementation (the external 3rd-party system)
2. __Fast vs. Slow:__ the Person resource resides in a system accessible by mulitple protocols. In
  this scenario, the `PersonCompositeDAO` object contains a reference to
    1. a TCP socket-based `PersonDAO` implementation (fast)
    2. an HTTP / XML-RPC `PersonDAO` implementation (slow)


