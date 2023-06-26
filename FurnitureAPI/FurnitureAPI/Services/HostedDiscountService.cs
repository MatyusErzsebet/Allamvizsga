using FurnitureAPI.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using System.Threading.Tasks;
using System.Threading;
using System;
using Microsoft.Extensions.Hosting;

namespace FurnitureAPI.Services
{
    public class HostedDiscountService: IHostedService
    {
        private Timer _timer;
        private readonly IServiceScopeFactory _serviceScopeFactory;

        public HostedDiscountService(IServiceScopeFactory serviceScopeFactory)
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

            foreach (var d in await context.Discounts.ToListAsync())
            {
                if (d.DeadLine < DateTime.Now)
                {
                    context.Entry(d).State = EntityState.Deleted;
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
