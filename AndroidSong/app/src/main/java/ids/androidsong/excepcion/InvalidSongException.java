package ids.androidsong.excepcion;

public class InvalidSongException extends RuntimeException {

    private String code;
    private String message;

    public InvalidSongException(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public enum Message{
        INVALID_TITLE("invalid_title","El titulo ingresado no es valido, es demasiado largo"),
        INVALID_AUTHOR("invalid_author","El nombre del autor ingresado no es valido, es demasiado largo"),
        INVALID_PRESENTATION("invalid_presentation","El campo presentacion es invalido, es demasiado largo"),
        INVALID_TONE("invalid_tone","El tono ingresado no es valido"),
        INVALID_TRANSPORTATION("invalid_transportation","El campo transporte tiene un valor invalido");

        private String code;
        private String description;

        Message(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
