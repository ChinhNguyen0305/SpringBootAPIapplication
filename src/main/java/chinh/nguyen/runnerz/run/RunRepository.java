package chinh.nguyen.runnerz.run;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RunRepository {
   private static final Logger log = LoggerFactory.getLogger(RunRepository.class);
   private final JdbcClient jdbcClient;

    public RunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

//    private List<Run> runs = new ArrayList<>();
//
//    List<Run> findAll (){
//        return runs;
//    }

//    Optional<Run> findById(Integer id){
////       return runs.get(id); //there is  a better way to do this
////    return runs.stream()
////            .filter(run ->run.id() == id)
////            .findFirst()
////            .get();
//        return runs.stream()
//                .filter(run -> run.id() == id)
//                .findFirst();
//    }
//
//    void create(Run run) {
//        runs.add(run);
//    }
//
//    void update(Run run, Integer id) {
//        Optional<Run> existingRun = findById(id);
//        existingRun.ifPresent(value -> runs.set(runs.indexOf(value), run));
//
////        Optional<Run> existingRun = findById(id);
////        if(existingRun.isPresent()){
////            runs.set(runs.indexOf(existingRun.get()),run);
////        }
//    }
//
//    void delete(Integer id) {
//        runs.removeIf(run->run.id().equals(id));
//    }
//
//    @PostConstruct
//    //Initialization
//
//    private void init(){
//        runs.add(new Run(1,
//                "Monday Morning Run",
//                LocalDateTime.now(),
//                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
//                3,
//                Location.INDOOR));
//
//        runs.add(new Run(2,
//                "WednesDay Evening Run",
//                LocalDateTime.now(),
//                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
//                6,
//                Location.INDOOR));
//    }

    //-------New way
    public List<Run> findAll(){
        return jdbcClient.sql("select * from run")
                .query(Run.class)
                .list();
    }

    public Optional<Run> findById (Integer id) {
        return jdbcClient.sql("select * from run where id = :id")
                .param("id", id)
                .query(Run.class)
                .optional();
    }

    public void create (Run run) {
        var updated = jdbcClient.sql("Insert into run(id, title,started_on,completed_on, miles,location) values (?,?,?,?,?,?)")
                .params(List.of(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString()))
                .update();
        //update(): execute the Sql and return the number of affected rows

        Assert.state(updated == 1, "Failed to create run" + run.title());

    }

    public void update (Run run, Integer id) {
    var updated = jdbcClient.sql ("update run set title = ?, started_on = ?, completed_on =?, miles =?, location =? where id =?")
            .params(List.of(run.title(),run.startedOn(),run.completedOn(),run.miles(),run.location().toString()),id)
            .update();

        Assert.state(updated == 1, "Failed to update run" + run.title());
    }

    public void delete (Integer id) {
    var updated = jdbcClient.sql("delete from run where id = :id")
            .param("id", id)
            .update();

    Assert.state(updated==1, "Fail to delete run " + id);
    }

    public int count () {
    return jdbcClient.sql("select * from run").query().listOfRows().size();
    }

    public void saveAll(List<Run> runs){
    runs.forEach(this::create);
    }

    public List<Run> findByLocation (String location) {
    return jdbcClient.sql("select * from run where location = :location")
            .param("location", location)
            .query(Run.class)
            .list();

    //Need to review this, param
    }



}
