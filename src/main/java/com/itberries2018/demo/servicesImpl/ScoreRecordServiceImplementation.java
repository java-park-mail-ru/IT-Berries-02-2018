package com.itberries2018.demo.servicesImpl;

import com.itberries2018.demo.Entities.History;
import com.itberries2018.demo.models.ScoreRecord;
import com.itberries2018.demo.Entities.User;
import org.springframework.stereotype.Service;
import com.itberries2018.demo.servicesIntefaces.ScoreRecordService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreRecordServiceImplementation implements ScoreRecordService {

    @Override
    public List<ScoreRecord> converUsersToSocreRecords(List<Object[]> results) {
        final List<ScoreRecord> records = new ArrayList<>();
//        for (User user : users) {
//            records.add(new ScoreRecord(user));
//        }

        for(Object[] hiAndpl : results){
            History entityHist = (History) hiAndpl[0];
            User entityUser = (User)hiAndpl[1];
            records.add(new ScoreRecord(entityUser, entityHist));
        }
        records.sort((o1, o2) -> {
            if (o1.getScore()<o2.getScore()){
                return  1;
            }else if(o1.getScore() == o2.getScore()){
                return 0;
            }else{
                return -1;
            }

        });
        for (int i = 0; i < records.size(); ++i) {
            records.get(i).setId(i);
        }
        return records;
    }
}
