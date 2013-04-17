composite-dao
=============

Demonstrates combination of Composite and DAO patterns using the Spring Framework.

What's the point?
-----------------

A composite DAO object enables transparent manipulation of persistent entities
that either reside in a local _system of reference_ (e.g. a 'local' database) or a remote
_system of record_ (e.g. a 3rd-party service provider). By wrapping source-specific DAOs in
a composite DAO, a dependent service can perform normal, simple interaction with a DAO instance
(the composite DAO) and **without** having to contain any source-specific logic.

Within the composite DAO, each encapsulated, source-specific DAO is responsible for all the
nitty-gritty particulars of interaction with its source. Over that, the composite DAO contains
simple logic such as:

1. Check component DAO 1. If result is non-null, return it. Otherwise,
2. Check component DAO 2.

When would I use this?
----------------------

A couple of example scenarios where composite DAO would benefit an application. These are only
examples - your experience and context may expose more scenarios!

1. **Local vs. Remote** a `Person` resource officially resides in a 3rd-party system outside the
  organization, and it is cached within the organization in an RDBMS. In this scenario, the
  `PersonCompositeDAO` object contains a reference to
    1. a JPA/Hibernate `PersonDAO` implementation
    2. a SOAP-based WS `PersonDAO` implementation
2. **Fast vs. Slow** a `Person` resource resides in a system accessible by mulitple protocols. In
  this scenario, the `PersonCompositeDAO` object contains a reference to
    1. a TCP socket-based `PersonDAO` implementation
    2. an HTTP / XML-RPC `PersonDAO` implementation


