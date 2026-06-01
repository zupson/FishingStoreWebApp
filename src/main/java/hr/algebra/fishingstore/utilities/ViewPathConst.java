package hr.algebra.fishingstore.utilities;

public class ViewPathConst {
    private ViewPathConst() {
    }

    private static final String PRODUCTS = "products";
    public static final String PRODUCTS_LIST = PRODUCTS + PathConst.LIST;
    public static final String PRODUCTS_DETAILS = PRODUCTS + PathConst.DETAILS;
    public static final String PRODUCTS_FORM_CREATE = PRODUCTS + PathConst.FORM_CREATE;
    public static final String PRODUCTS_FORM_UPDATE = PRODUCTS + PathConst.FORM_UPDATE;

    private static final String CATEGORIES = "categories";
    public static final String CATEGORIES_LIST = CATEGORIES + PathConst.LIST;
    public static final String CATEGORIES_DETAILS = CATEGORIES + PathConst.DETAILS;
    public static final String CATEGORIES_FORM_CREATE = CATEGORIES + PathConst.FORM_CREATE;
    public static final String CATEGORIES_FORM_UPDATE = CATEGORIES + PathConst.FORM_UPDATE;

    private static final String ADDRESSES = "addresses";
    public static final String ADDRESSES_LIST = ADDRESSES + PathConst.LIST;
    public static final String ADDRESSES_FORM_CREATE = ADDRESSES + PathConst.FORM_CREATE;

    private static final String AUTH = "auth";
    public static final String AUTH_REGISTER_VIEW = AUTH + PathConst.REGISTER;
    public static final String AUTH_LOGIN_VIEW = AUTH + PathConst.LOGIN;

    public static final String CARTS_DETAILS_VIEW = "carts" + PathConst.DETAILS;

    public static final String CART_PRODUCTS_LIST_VIEW = "cart-products" + PathConst.LIST;

    private static final String LOGIN_HISTORIES = "login-histories";
    public static final String LOGIN_HISTORY_LIST_VIEW = LOGIN_HISTORIES + PathConst.LIST;
    public static final String LOGIN_HISTORY_DETAILS_VIEW = LOGIN_HISTORIES + PathConst.DETAILS;

    private static final String ORDERS = "orders";
    public static final String ORDERS_LIST_VIEW = ORDERS + PathConst.LIST;
    public static final String ORDERS_DETAILS_VIEW = ORDERS + PathConst.DETAILS;
    public static final String ORDERS_FORM_CREATE_VIEW = ORDERS + PathConst.FORM_CREATE;
    public static final String ORDERS_FORM_UPDATE_VIEW = ORDERS + PathConst.FORM_UPDATE;

    private static final String PAYMENTS = "payments";
    public static final String PAYMENTS_LIST_VIEW = PAYMENTS + PathConst.LIST;
    public static final String PAYMENTS_DETAILS_VIEW = PAYMENTS + PathConst.DETAILS;
    public static final String PAYMENTS_FORM_CREATE_VIEW = PAYMENTS + PathConst.FORM_CREATE;
    public static final String PAYMENTS_FORM_UPDATE_VIEW = PAYMENTS + PathConst.FORM_UPDATE;

    private static final String PRODUCT_ORDERS = "product-orders";
    public static final String PRODUCT_ORDERS_LIST_VIEW = PRODUCT_ORDERS + PathConst.LIST;
    public static final String PRODUCT_ORDERS_DETAILS_VIEW = PRODUCT_ORDERS + PathConst.DETAILS;
    public static final String PRODUCT_ORDERS_FORM_CREATE_VIEW = PRODUCT_ORDERS + PathConst.FORM_CREATE;
    public static final String PRODUCT_ORDERS_FORM_UPDATE_VIEW = PRODUCT_ORDERS + PathConst.FORM_UPDATE;

    private static final String USERS = "users";
    public static final String USER_LIST_VIEW = USERS + PathConst.LIST;
    public static final String USER_DETAILS_VIEW = USERS + PathConst.DETAILS;
    public static final String USER_FORM_UPDATE_VIEW = USERS + PathConst.FORM_UPDATE;
}