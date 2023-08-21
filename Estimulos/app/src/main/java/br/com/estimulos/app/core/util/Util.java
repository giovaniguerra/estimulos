package br.com.estimulos.app.core.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import br.com.estimulos.dominio.IEntidade;

/**
 * Classe responsavel por lidar com tratamento de imagem para aplicacao
 *
 * Mais sobre essa classe em: http://developer.android.com/intl/pt-br/training/displaying-bitmaps/load-bitmap.html.
 *
 * Created by Giovani on 01/04/2016.
 */
public class Util {
    /** Constantes
     */
    /** A proporção utilizada como base para criação do layout de jogo
     * Tamanho original de tela utilizado : 800x600 = 1.33f
     *                                      1280x574 = 2.22f*/
    public final static float PROPORCAO_LARGURA = 1.33f;

    /** Variaveis
     */
    // Variaveis
    /** Variavel para guardar um bitmap temporario*/
    private static Bitmap mPlaceHolderBitmap;

    private static Pattern pattern;
    private static Matcher matcher;


    /**
     * Construtor
     */
    private Util(){}

    /** Classes estaticas
     */
    // Classes estaticas

    /**
     * Classe responsavel por salvar uma referencia de um BitmapWorkerTask
     */
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * Classe responsavel por adicionar um bitmap a um ImageView rodando em uma thread
     * em background.
     */
    static class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private Context context;
        private String data = "";

        public BitmapWorkerTask(Context context, ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            this.context = context;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            data = params[0];
            // TODO : Investigar NullPointerException no data!
            // TODO : Hardcode o 100, 100...
            return decodeSampledBitmapFromUri(Uri.parse(data), 100, 100);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    this.cancel(true);
                }
            }
        }
    }

    /** Metodos estaticos
     */
    // Metodos estaticos

    /**
     * Metodo responsavel por iniciar o carregamento de um bitmap dentro de um ImageView
     * @param context Contexto da aplicacao
     * @param uri Uri contendo a localizacao do arquivo
     * @param imageView ImageView onde sera colocado o Bitmap
     */
    public static void carregarBitmap(Context context, String uri, ImageView imageView) {
        // imageView.setImageBitmap(decodeSampledBitmapFromUri(Uri.parse(uri), 100, 100));

        if (cancelPotentialWork(uri, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(context, imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(uri);
        }
    }

    /**
     * Metodo para criar um bitmap vindo de um recurso, dado um tamanho especifico
     * @param uri Caminho do arquivo
     * @param reqWidth largura a qual pretende aproximar a imagem
     * @param reqHeight altura a qual pretende aproximar a imagem
     * @return o Bitmap criado com o tamanho especificado
     */
    private static Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.toString(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri.toString(), options);
    }

//    /**
//     * Metodo para criar um bitmap vindo de um recurso, dado um tamanho especifico
//     * @param res Recurso onde eh possivel encontrar a imagem
//     * @param reqWidth largura a qual pretende aproximar a imagem
//     * @param reqHeight altura a qual pretende aproximar a imagem
//     * @return o Bitmap criado com o tamanho especificado
//     */
//    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//                                                         int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
//    }

    /**
     * Método para calcular o aumento ou reducao de uma imagem.
     * @param options options que contem a largura e altura de um bitmap
     * @param reqWidth largura a qual pretende aproximar a imagem
     * @param reqHeight altura a qual pretende aproximar a imagem
     * @return Um valor inteiro indicando quantas vezes a imagem sera aumentada ou diminuida
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Metodo responsavel por testar se ja ha alguma tarefa rodando para aquele ImageView para
     * prevencao de retrabalho, reduzindo entao o processamento
     * @param data a Uri contendo o caminho da imagem
     * @param imageView o ImagemView onde sera colocado a imagem
     * @return Um valor booleano indicando se deve ou nao cancelar a tarefa
     *          TRUE - Indica que deve cancelar
     *          False - Indica que nao eh necessario cancelar
     */
    private static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData.isEmpty() || !bitmapData.equals(data)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    /**
     * Metodo utilizado para recuperar a tarefa que esta relacionada a um determinado ImageView
     * @param imageView o ImageView que sera procurado
     * @return A thread responsavel por tratar o bitmap neste ImageView
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }



    /** VER
     * http://developer.android.com/intl/pt-br/training/displaying-bitmaps/load-bitmap.html
     * http://android-developers.blogspot.com.br/2010/07/multithreading-for-performance.html
     *
     * esse metodo getThumbnail cria a imagem com uma qualidade melhor ao que me lembro
     * */
//    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
//        InputStream input = context.getContentResolver().openInputStream(uri);
//
//        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
//        onlyBoundsOptions.inJustDecodeBounds = true;
//        onlyBoundsOptions.inDither=true;//optional
//        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
//        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
//        input.close();
//        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
//            return null;
//
//        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
//
//        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;
//
//        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
//        bitmapOptions.inDither=true;//optional
//        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
//        input = context.getContentResolver().openInputStream(uri);
//        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
//        input.close();
//        return bitmap;
//    }
//
//    private static int getPowerOfTwoForSampleRatio(double ratio){
//        int k = Integer.highestOneBit((int) Math.floor(ratio));
//        if(k==0) return 1;
//        else return k;
//    }
//
//
//
//
//
//
//
//
//
//    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // Testar as escalas para garantir que o objeto nao seja distorcido
//        float finalScale;
//        if(scaleWidth < scaleHeight)
//            finalScale = scaleWidth;
//        else
//            finalScale = scaleHeight;
//
//        // CREATE A MATRIX FOR THE MANIPULATION
//        Matrix matrix = new Matrix();
//        // RESIZE THE BIT MAP
//        matrix.postScale(finalScale, finalScale);
//
//        // "RECREATE" THE NEW BITMAP
//        Bitmap resizedBitmap = Bitmap.createBitmap(
//                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
//        return resizedBitmap;
//    }

/** FIM METODOS DE IMAGEM*/
//

    //Email Pattern
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\." +
        "[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validateEmail(String email) {
        return true;
      /*  pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();*/

    }
    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return true;
        /*return txt!=null && txt.trim().length()>0 ? true: false;*/
    }

    /**
     * TODO javadoc
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /**
        //For 3G check
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        //For WiFi Check
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        */

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * TODO javadoc
     * @param entidade
     * @param <T>
     * @return
     */
    public static <T extends IEntidade> String toJson(T entidade){
        Gson gson = new Gson();
        return gson.toJson(entidade);
    }

    /**
     * TODO javadoc
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends IEntidade> T fromJson(String json, Class<? extends T> type){
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * TODO javadoc
     * @param entidade
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T extends IEntidade> byte[] entidadeToBytes(final T entidade)
            throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final GZIPOutputStream gz = new GZIPOutputStream(baos);
        final ObjectOutputStream oos = new ObjectOutputStream(gz);

        try {
            oos.writeObject(entidade);
            // usar o finish para que seja criado o EOF do arquivo.
            gz.finish();
            oos.flush();
            return baos.toByteArray();
        }finally {
            oos.close();
            gz.close();
            baos.close();
        }
    }

    /**
     * TODO javadocs
     * @param bytesEntidade
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T extends IEntidade> T bytesToEntidade(byte[] bytesEntidade)
            throws IOException, ClassNotFoundException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytesEntidade);
        final GZIPInputStream gs = new GZIPInputStream(bais);
        final ObjectInputStream ois = new ObjectInputStream(gs);
        try {
            @SuppressWarnings("unchecked")
            T expandedObject = (T) ois.readObject();
            return expandedObject;
        } finally {
            ois.close();
            gs.close();
            bais.close();
        }
    }

    /**
     * TODO javadocs
     * @param bytes
     * @return
     */
    public static String bytesToBase64String(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * TODO javadocs
     * @param encodedString
     * @return
     */
    public static byte[] base64StringToBytes(String encodedString){
        return Base64.decode(encodedString, Base64.NO_WRAP);
    }

}
