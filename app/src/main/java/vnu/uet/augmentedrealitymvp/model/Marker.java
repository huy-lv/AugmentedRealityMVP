package vnu.uet.augmentedrealitymvp.model;

/**
 * Created by hienbx94 on 3/21/16.
 */
public class Marker {
    int _id;
    String _name;
    String _image;
    String _iset;
    String _fset;
    String _fset3;
    String created_at;
    String user_name;

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public void set_created_at(String created_at) {
        this.created_at = created_at;
    }

    public void set_iset(String _iset) {
        this._iset = _iset;
    }

    public void set_fset(String _fset) {
        this._fset = _fset;
    }

    public void set_fset3(String _fset3) {
        this._fset3 = _fset3;
    }

    public void set_user_name(String _fset3) {
        this.user_name = user_name;
    }

    public String get_name() {

        return _name;
    }

    public String get_image() {
        return _image;
    }

    public Marker(){

    }

    public Marker(int _id, String _name, String _image, String _iset, String _fset, String _fset3, String created_at, String user_name) {
        this._id = _id;
        this._name = _name;
        this._image = _image;
        this._iset = _iset;
        this._fset = _fset;
        this._fset3 = _fset3;
        this.created_at = created_at;
        this.user_name = user_name;
    }

    public String get_iset() {
        return _iset;
    }

    public String get_fset() {
        return _fset;
    }

    public String get_fset3() {
        return _fset3;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUserName() {
        return user_name;
    }

    public int get_id() {

        return _id;
    }
}
