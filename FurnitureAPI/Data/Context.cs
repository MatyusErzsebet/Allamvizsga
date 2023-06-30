using FurnitureAPI.Data.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Reflection.Emit;

namespace FurnitureAPI.Data
{
    public class Context: DbContext
    {
        public DbSet<UserEntity> Users { get; set; }
        public DbSet<UnconfirmedUserEntity> UnconfirmedUsers { get; set; }
        public DbSet<FurnitureTypeEntity> FurnitureTypes { get; set; }
        public DbSet<FurnitureEntity> Furnitures { get; set; }
        public DbSet<ReviewEntity> Reviews { get; set; }
        public DbSet<DiscountEntity> Discounts { get; set; }
        public DbSet<PurchaseEntity> Purchases { get; set; }

        public DbSet<UserForgotPasswordKeyEntity> UserForgotPasswordKeys { get; set; }


        public Context(DbContextOptions<Context> options) : base(options)
        {

        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            builder.Entity<UserEntity>()
            .HasIndex(u => u.Email)
            .IsUnique();

            builder.Entity<UnconfirmedUserEntity>()
            .HasIndex(u => u.Email)
            .IsUnique();

            builder.Entity<UserForgotPasswordKeyEntity>()
                .HasIndex(up => up.UserId)
                .IsUnique();

            builder.Entity<FurnitureTypeEntity>()
                .HasIndex(ft => ft.Name)
                .IsUnique();

            builder.Entity<PurchaseEntity>()
                .HasOne<UserEntity>(p => p.User)
                .WithMany(u => u.Purchases)
                .HasForeignKey(p => p.UserId);


            builder.Entity<PurchaseEntity>()
                .HasOne<FurnitureEntity>(p => p.Furniture)
                .WithMany(f => f.Purchases)
                .HasForeignKey(p => p.FurnitureId);

            builder.Entity<ReviewEntity>()
                .HasOne<UserEntity>(r => r.User)
                .WithMany(u => u.Reviews)
                .HasForeignKey(r => r.UserId);


            builder.Entity<ReviewEntity>()
                .HasOne<FurnitureEntity>(r => r.Furniture)
                .WithMany(f => f.Reviews)
                .HasForeignKey(p => p.FurnitureId);

            builder.Entity<DiscountEntity>()
                .HasIndex(d => d.FurnitureId)
                .IsUnique();


        }
    }
}
