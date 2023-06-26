using System.IdentityModel.Tokens.Jwt;
using System;
using System.Linq;

namespace FurnitureAPI.Utils
{
    public static class TokenClaimHandler
    {
        public static string GetJWTTokenClaim(string token, string claimName)
        {
            try
            {
                var tokenHandler = new JwtSecurityTokenHandler();
                var securityToken = (JwtSecurityToken)tokenHandler.ReadToken(token);
                var claimValue = securityToken.Claims.FirstOrDefault(c => c.Type == claimName)?.Value;
                return claimValue;
            }
            catch (Exception)
            {
                //TODO: Logger.Error
                return null;
            }
        }
    }
}
