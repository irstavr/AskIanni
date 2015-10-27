IanniRetrievalSystem
====================

IanniRetrievalSystem is a modern search engine for famous last words.
It is a complete Information Retrieval System based on the Universal WordNet (UWN), 
a big lexicographical database for various different languages.

IanniRetrievalSystem can support english and greek queries by the user on its search engine.
It contains a query processor and ranking functions for sorting the results by taking into account 
the synonyms, hyponyms, meronyms of every word/phrase and their weights on the UWN library. 
For the best precision of the system, IanniRetrievalSystem takes advantage of the classification 
of the synonyms of every term into different 'senses'.

Processes:

Indexing (Vocabulary file, Posting file, Documents file)
Query valuation (Vector Space Model, Okapi BM25)
Query Processor (English & Greek queries, Optional term weight by user)
Ranking Functions (based on UWN)

Ask 'Ianni' , it knows ;)
