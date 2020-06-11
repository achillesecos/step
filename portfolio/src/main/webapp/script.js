// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Transition delay in miliseconds
const DELAY = 4000;

// An array of objects such that each object contains a path with its
// associated description
const IMAGE_ARRAY = [
    {
        path: "images/image1.png",
        description: "Carnegie Mellon Hamerschlag Hall, Fall 2018"
    },
    {
        path: "images/image2.png",
        description: "Los Angeles, Spring 2018"
    },
    {
        path: "images/image3.png",
        description: "Aerial view of Pittsburgh from Cathedral of Learning, Fall 2018"
    },
    {
        path: "images/image4.png",
        description: "Google Mountain View, Summer 2019"
    },
    {
        path: "images/image5.png",
        description: "San Francisco, Summer 2019"
    },
    {
        path: "images/image6.png",
        description: "Google Kirkland, Summer 2019"
    },
    {
        path: "images/image7.png",
        description: "Downtown Seattle seen from Space Needle, Summer 2019"
    },
    {
        path: "images/image8.png",
        description: "Wallace Falls State Park, Summer 2019"
    },
    {
        path: "images/image9.png",
        description: "Downtown Vancouver seen from water plane, Summer 2019"
    },
    {
        path: "images/image10.png",
        description: "Downtown Seattle seen from Kerry Park, Summer 2019"
    }
]

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['-Batman Quote', '-Superman Quote', '-Starwars Quote', '-Avengers Quote'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/* Transitions through images with the onload event */
window.onload = function() {
    
    var image = document.getElementById("imgID");
    var textBox = document.getElementById("imgText");
    
    // accounts for the first iteration that sets index to 0
    var index = -1;
    
    function transition() {
        
        index = (index + 1) % IMAGE_ARRAY.length;
        image.src = IMAGE_ARRAY[index].path;
        textBox.innerHTML = IMAGE_ARRAY[index].description;
    }
    
    setInterval(transition, DELAY);
}
