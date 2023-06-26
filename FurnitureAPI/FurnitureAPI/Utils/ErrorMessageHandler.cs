using Microsoft.AspNetCore.Mvc.ModelBinding;
using System.Collections.Generic;
using System.Linq;

namespace FurnitureAPI.Utils
{
    public static class ErrorMessageHandler
    {
        public static string CreateErrorMessagesForProperties(ModelStateDictionary modelState)
        {
            var errors = modelState.Select(x => new { Key = x.Key, Value = x.Value.Errors })
                .Where(y => y.Value.Count > 0)
                .ToList();

            var errorsMap = new Dictionary<string, List<string>>();

            foreach (var propErrors in errors)
            {
                var propertyErrors = new List<string>();
                foreach (var err in propErrors.Value)
                {
                    propertyErrors.Add(err.ErrorMessage);
                }

                if (string.Equals(propErrors.Key, string.Empty))
                    errorsMap.Add("server", propertyErrors);
                else
                    errorsMap.Add(propErrors.Key, propertyErrors);

            }

            return errorsMap.First().Value.First();

        }

    }
}
