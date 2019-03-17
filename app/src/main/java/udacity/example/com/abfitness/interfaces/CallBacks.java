package udacity.example.com.abfitness.interfaces;

public interface CallBacks {
    void callbackObserver(Object obj);

    interface playerCallBack {
        void onItemClickOnItem(Integer albumId);

        void onPlayingEnd();
    }
}
