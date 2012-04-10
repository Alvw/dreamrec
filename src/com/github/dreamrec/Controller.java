package com.github.dreamrec;

/**
 *
 */
public class Controller implements DataListener{

    Model model;
    IncomingDataProvider dataProvider;

    public Controller(Model model) {
        this.model = model;
    }

    public void saveToFile(){
        //todo
    }

    public void readFromFile(){
       //todo
    }

   void StartRecording(){
       //todo
   }

    void StopRecording(){
       //todo
    }

    public void dataReceived(int value) {
        throw new UnsupportedOperationException("todo");
    }
}
