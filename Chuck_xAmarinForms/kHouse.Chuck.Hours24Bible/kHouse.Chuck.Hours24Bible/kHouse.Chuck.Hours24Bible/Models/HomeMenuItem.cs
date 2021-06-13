using System;
using System.Collections.Generic;
using System.Text;

namespace kHouse.Chuck.Hours24Bible.Models
{
    public enum MenuItemType
    {
        Browse,
        About
    }
    public class HomeMenuItem
    {
        public MenuItemType Id { get; set; }

        public string Title { get; set; }
    }
}
