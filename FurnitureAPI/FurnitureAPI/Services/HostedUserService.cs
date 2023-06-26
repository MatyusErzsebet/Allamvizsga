using FurnitureAPI.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace FurnitureAPI.Services
{
    public class HostedUserService : IHostedService
    {
        private Timer _timer;
        private readonly IServiceScopeFactory _serviceScopeFactory;

        public HostedUserService(IServiceScopeFactory serviceScopeFactory)
        {
            _timer = new Timer(DeleteUnconfirmedUsersWhenValidationKeyExpired, null, TimeSpan.Zero,
            TimeSpan.FromSeconds(5));
            _serviceScopeFactory = serviceScopeFactory;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _timer = new Timer(DeleteUnconfirmedUsersWhenValidationKeyExpired, null, TimeSpan.Zero,
             TimeSpan.FromMinutes(1));

            return Task.CompletedTask;
        }

        public async void DeleteUnconfirmedUsersWhenValidationKeyExpired(object state)
        {
            var context = _serviceScopeFactory.CreateScope().ServiceProvider.GetService<Context>();

            bool isAnyItemModified = false;

            foreach (var uu in await context.UnconfirmedUsers.ToListAsync())
            {
                if (uu.ValidationKeyExpirationDate < DateTime.Now)
                {
                    context.Entry(uu).State = EntityState.Deleted;
                    isAnyItemModified = true;
                }             
            }

            foreach (var up in await context.UserForgotPasswordKeys.ToListAsync())
            {
                if (up.KeyExpirationDate < DateTime.Now)
                {
                    context.Entry(up).State = EntityState.Deleted;
                    isAnyItemModified = true;
                }
            }

            if (isAnyItemModified == true)
            {
                try
                {
                    await context.SaveChangesAsync();
                }
                catch (Exception ex)
                {

                }
            }
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            return Task.CompletedTask;
        }
    }
}
