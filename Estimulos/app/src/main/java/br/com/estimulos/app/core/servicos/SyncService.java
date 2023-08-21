package br.com.estimulos.app.core.servicos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class SyncService extends Service {

    public SyncService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        while(true) {
            // Comunica com o servidor.
            // Se tiver que atualizar, envia dados ou recebe dados.
            try {
                Thread.sleep(50000);
            } catch(Exception error) {
            }
        }
        // ou executar uma vez só e agendar a execuão do serviço para daqui a x minutos (alarm)
//        return super.onStartCommand(intent, flags, startId);
    }
}
