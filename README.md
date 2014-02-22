Zappos
======

ZappProducts is a simple application that allows user to enter to inputs: Quantity and Cost.
The application makes use of Zappos Search API to create a list of desired 'Quantity' of gifts from Zappos.com whose combined values match as closely as possible to the 'Cost'.
The application displays the top five (any desired number of results can be displayed) combination of products.

The application makes use of [GSON library] (https://code.google.com/p/google-gson/) to parse the JSON results from Zappos API and convert them into Java objects.

The GSON library needs to be linked to run the project.
