# AskIanni
###################################################################################################

AskIanni is a modern search engine for famous last words!
A complete Information Retrieval & crawler system for documents or web pages,
based on the Universal WordNet (UWN), on of the biggest lexicographical databases
for various different languages.

AskIanni can support english and greek queries by the user on its search engine.
It contains a query processor and ranking functions for sorting the results by taking into account 
the synonyms, hyponyms, meronyms of every word/phrase and their weights on the UWN library. 
For the best precision of the system, IanniRetrievalSystem takes advantage of the classification 
of the synonyms of every term into different 'senses'.

## Processes: ##

Indexing (Vocabulary file, Posting file, Documents file)
Query valuation (Inverted index, Vector Space Model, Okapi BM25, Stemming)
Query Processor (English & Greek queries, Optional term weight by user)
Ranking Functions (based on UWN classification and user's weights on every term)

Ask 'Ianni' , 'it' knows ;)
