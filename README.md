# transcendentalism

A Clojure library that generates the Transcendental Metaphysics website.

## Usage

lein run

OR

lein uberjar
(to create a standalone JAR file suitable for deployment)

Allowed integer command flags: `server` (to start at a port other than 80).
If `sync` flag is set, will sync static resources (i.e. images) to the cloud
instead of starting a server.

## License

Copyright © 2020 Daniel Gierl, All rights reserved

TODO - talk to a copyright lawyer to determine what license would be appropriate

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
