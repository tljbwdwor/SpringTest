package iths.tlj.lab2i;

public class Mapper {
    private GuitaristRestModel mapPersistenceModelToRestModel(Guitarist guitarist) {
        GuitaristRestModel guitaristRestModel = new GuitaristRestModel();
        guitaristRestModel.setId(guitarist.getId());
        guitaristRestModel.setFirstname(guitarist.getFirstName());
        guitaristRestModel.setLastname(guitarist.getLastName());
        guitaristRestModel.setNationality(guitarist.getNationality());
        return guitaristRestModel;
    }

    private void mapRestModelToPersistenceModel(GuitaristRestModel guitaristRestModel, Guitarist guitarist){
        guitarist.setFirstName(guitaristRestModel.getFirstname());
        guitarist.setLastName(guitaristRestModel.getLastname());
        guitarist.setNationality(guitaristRestModel.getNationality());
        guitarist.setId(guitaristRestModel.getId());
    }
}
