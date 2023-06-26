namespace FurnitureAPI.Services.ServiceResponses
{
    public enum UserServiceResponses
    {
        SUCCESS = 200,
        ERROR = 500,
        BADREQUEST = 400,
        NOTFOUND = 404,
        USERALREADYEXISTS = 409,
        EMAIL_NOT_FOUND = 4041,
        VALIDATION_KEY_NOT_FOUND = 4042,
        EMAILNOTSENT = 5001,
        FORGOTPASSWORDNOTSETTOUSER = 4001,
        PASSWORDVALIDATIONKEYNOTMATCHEDBEFORE = 4002
    }
}
