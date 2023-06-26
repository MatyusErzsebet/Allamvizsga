using System;

namespace FurnitureAPI.Utils
{
    public static class ValidationKeyGenerator
    {
        private static readonly int _emailVerificationKeyLength = 100;
        private static readonly int _forgotPasswordKeyLength = 8;
        private static readonly Random _random = new Random();
        private static int ordA = 'A';
        private static int ordZ = 'Z';
        public static string GenerateValidationKey()
        {
            
            char[] chars= new char[_emailVerificationKeyLength];
            for (int i = 0; i< _emailVerificationKeyLength; i++)
            {
                int generatedNb = _random.Next(ordA, ordZ);
                chars[i] = (char)generatedNb;
            }
            return new string(chars);
        }

        public static string GenerateForgotPasswordKey()
        {
            int ordA = 'A';
            int ordZ = 'Z';
            char[] chars = new char[_forgotPasswordKeyLength];
            for (int i = 0; i < _forgotPasswordKeyLength; i++)
            {
                int generatedNb = _random.Next(ordA, ordZ);
                chars[i] = (char)generatedNb;
            }
            return new string(chars);
        }
    }
}
