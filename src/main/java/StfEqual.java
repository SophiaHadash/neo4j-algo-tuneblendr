import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;


/**Satisfaction function module: equal. */
public class StfEqual {

    // procedure label
    public static final String label = "TBL_STF_EQUAL";

    // This field declares that we need a GraphDatabaseService
    // as context when any procedure in this class is invoked
    @Context
    public GraphDatabaseService db;

    // This gives us a log instance that outputs messages to the
    // standard log, normally found under `data/log/console.log`
    @Context
    public Log log;


    @Procedure(value = "tbl.stf.equal")
    @Description("Execute satisfaction function 'equal'")
    public void execute(@Name("configId") String configId,
                        @Name("groupId") String groupId) {

        Transaction tx = db.beginTx();
        try{
            //tx.acquireWriteLock();

            // find config
            Node config = tx.findNode(Label.label("STF_CONFIG"), "id", configId);

            // find group
            Node group = tx.findNode(Label.label("Group"), "id", groupId);

            Iterable<Relationship> iter = group.getRelationships(Direction.OUTGOING, RelationshipType.withName("MEMBER"));
            while(iter.iterator().hasNext()){
                Relationship r = iter.iterator().next();
                r.setProperty(StfEqual.label+"_"+configId+"_weight", config.getProperty("weight"));
            }
            tx.commit();
        }
        finally {
            tx.terminate();
            tx.close();
        }
    }
}
