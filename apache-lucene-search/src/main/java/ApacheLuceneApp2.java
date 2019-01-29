import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class ApacheLuceneApp2 {
    public static void main(String[] args) throws IOException {
        Directory directory = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());

        Document document = new Document();
        document.add(new TextField("title", "Book about dogs", Field.Store.YES));
        document.add(new TextField("body", "Ala has a dog", Field.Store.YES));
        indexWriter.addDocument(document);

        Document document2 = new Document();
        document2.add(new TextField("title", "Book about cats", Field.Store.YES));
        document2.add(new TextField("body", "Óła has a cat", Field.Store.YES));
        indexWriter.addDocument(document2);
        indexWriter.commit();

        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Query query = new BooleanQuery.Builder()
                .add(new TermQuery(new Term("title", "dogs")), BooleanClause.Occur.SHOULD)
                .add(new TermQuery(new Term("title", "cats")), BooleanClause.Occur.SHOULD)
                .build();

       Query query2 = new BooleanQuery.Builder()
                .add(query, BooleanClause.Occur.MUST)
                .add(new TermQuery(new Term("body", "óła")), BooleanClause.Occur.MUST)
                .build();

        TopDocs search = indexSearcher.search(query2, 10);

        for (ScoreDoc scoreDoc : search.scoreDocs) {
            System.out.println(scoreDoc.score + " " +
                    indexSearcher.doc(scoreDoc.doc).get("body"));
        }
    }
}
