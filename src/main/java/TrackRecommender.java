import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.neo4j.helpers.collection.MapUtil.stringMap;

/**
 * This is an example showing how you could expose Neo4j's full text indexes as
 * two procedures - one for updating indexes, and one for querying by label and
 * the lucene query language.
 */
public class TrackRecommender {

    // Only static fields and @Context-annotated fields are allowed in
    // Procedure classes. This static field is the configuration we use
    // to create full-text indexes.
    private static final Map<String, String> FULL_TEXT =
            stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext");

    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;

    /**
     * This declares the first of two procedures in this class - a
     * procedure that performs queries in a legacy index.
     * <p>
     * It returns a Stream of Records, where records are
     * specified per procedure. This particular procedure returns
     * a stream of {@link SearchHit} records.
     * <p>
     * The arguments to this procedure are annotated with the
     * {@link Name} annotation and define the position, name
     * and type of arguments required to invoke this procedure.
     * There is a limited set of types you can use for arguments,
     * these are as follows:
     *
     * <ul>
     *     <li>{@link String}</li>
     *     <li>{@link Long} or {@code long}</li>
     *     <li>{@link Double} or {@code double}</li>
     *     <li>{@link Number}</li>
     *     <li>{@link Boolean} or {@code boolean}</li>
     *     <li>{@link Map} with key {@link String} and value {@link Object}</li>
     *     <li>{@link List} of elements of any valid argument type, including {@link List}</li>
     *     <li>{@link Object}, meaning any of the valid argument types</li>
     * </ul>
     *
     * @param userId the user identifier
     * @param depth the depth of the algorithm. Search time increases exponentially.
     * @return the nodes found by the query
     */
    @Procedure(value = "tbl.recommend.track")
    @Description("Execute track recommendation algorithm, return found nodes with ratings.")
    public Stream<SearchHit> search(@Name("userId") String userId,
                                    @Name("depth") Double depth) {

        //Node u = db.findNode(Label.label("User"), "id", userId);

        // just return the user
        return db.findNodes(Label.label("User"), "id", userId)
                .stream()
                .map(SearchHit::new);
    }

    /**
     * This is the output record for our search procedure. All procedures
     * that return results return them as a Stream of Records, where the
     * records are defined like this one - customized to fit what the procedure
     * is returning.
     * <p>
     * These classes can only have public non-final fields, and the fields must
     * be one of the following types:
     *
     * <ul>
     *     <li>{@link String}</li>
     *     <li>{@link Long} or {@code long}</li>
     *     <li>{@link Double} or {@code double}</li>
     *     <li>{@link Number}</li>
     *     <li>{@link Boolean} or {@code boolean}</li>
     *     <li>{@link Node}</li>
     *     <li>{@link org.neo4j.graphdb.Relationship}</li>
     *     <li>{@link org.neo4j.graphdb.Path}</li>
     *     <li>{@link Map} with key {@link String} and value {@link Object}</li>
     *     <li>{@link List} of elements of any valid field type, including {@link List}</li>
     *     <li>{@link Object}, meaning any of the valid field types</li>
     * </ul>
     */
    public static class SearchHit {

        // This records contain a single field named 'nodeId'
        public long nodeId;

        public SearchHit(Node node) {
            this.nodeId = node.getId();
        }
    }
}
