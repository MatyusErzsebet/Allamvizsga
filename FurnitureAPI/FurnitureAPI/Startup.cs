using AutoMapper;
using FurnitureAPI.AutoMappres;
using FurnitureAPI.Data;
using FurnitureAPI.DataAccesLayer;
using FurnitureAPI.Mappers;
using FurnitureAPI.Models;
using FurnitureAPI.Services;
using FurnitureAPI.Utils;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FurnitureAPI
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        public void ConfigureServices(IServiceCollection services)
        {

            services.AddControllers()
                .ConfigureApiBehaviorOptions(options =>
                {
                    options.InvalidModelStateResponseFactory = context =>
                    {
                        var result = new BadRequestObjectResult(new BackendResponse<string>(ErrorMessageHandler.CreateErrorMessagesForProperties(context.ModelState)));


                        return result;
                    };


                });

            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "FurnitureAPI", Version = "v1" });
            });

            services.AddDbContext<Context>(options =>
                options.UseSqlServer(Configuration.GetConnectionString("DefaultConnection")));

            services.AddTransient<IUserService, UserService>();
            services.AddTransient<IReviewService, ReviewService>();
            services.AddTransient<IFurnitureService, FurnitureService>();
            services.AddTransient<IPurchaseService, PurchaseService>();

            services.AddHostedService<HostedUserService>();
            services.AddHostedService<HostedDiscountService>();
            services.AddControllers();

            services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme).AddJwtBearer(options => {
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = true,
                    ValidateAudience = true,
                    ValidateLifetime = true,
                    ValidateIssuerSigningKey = true,
                    ValidIssuer = Configuration["Jwt:Issuer"],
                    ValidAudience = Configuration["Jwt:Audience"],
                    IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Configuration["Jwt:Key"]))
                };
                options.Events = new JwtBearerEvents
                {
                    OnMessageReceived = ctx => {
                        if (ctx.Request.Headers.ContainsKey("Token"))
                        {
                            var bearerToken = ctx.Request.Headers["Token"].ElementAt(0);
                            var token = bearerToken.StartsWith("Bearer") ? bearerToken.Substring(0) : bearerToken;
                            ctx.Token = token;
                        }
                        return Task.CompletedTask;
                    },

                    OnChallenge = context =>
                    { 

                        context.HandleResponse();

                        var payload = new BackendResponse<string>("Invalid token");
                        
                        var json = JsonConvert.SerializeObject(payload);

                        context.Response.ContentType = "application/json";
                        context.Response.StatusCode = 401;

                        return context.Response.WriteAsync(json);
                    },
                    OnForbidden = context =>
                    {

                        var payload = new BackendResponse<string>("Forbidden");

                        var json = JsonConvert.SerializeObject(payload);

                        context.Response.ContentType = "application/json";
                        context.Response.StatusCode = 403;

                        return context.Response.WriteAsync(json);
                    },
                };
            });
        
                

            var mapperConfig = new MapperConfiguration(mc =>
            {
                mc.AddProfile(new UserMappingProfile());
                mc.AddProfile(new ReviewMappingProfile());
                mc.AddProfile(new FurnitureMappingProfile());
                mc.AddProfile(new PurchaseMappingProfile());
            });

            IMapper mapper = mapperConfig.CreateMapper();
            services.AddSingleton(mapper);

        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "FurnitureAPI v1"));
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthentication();
            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });

            
        }
    }
}
