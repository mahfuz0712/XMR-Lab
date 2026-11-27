# XMR Lab

**Linux. Without Limits.**

XMR Lab turns an Android tablet or phone into a full Linux-based security research
workstation — no PC required. It boots a real Debian environment on-device (through a
rootless virtual machine, User-Mode Linux, or a rooted chroot, depending on your device)
and gives you a tablet-first toolkit for Wi-Fi auditing, network scanning, USB
HID/gadget tools, and a full Linux terminal, all in one app.

> **For authorized security testing and research only.** Only use XMR Lab against
> networks, devices, and systems you own or have explicit permission to test. You are
> responsible for complying with the laws that apply to you.

## What you can do with it

- **A real Linux environment on your device** — a Debian (arm64) system you can `apt install`
  into, with a full terminal, persistent storage, and SSH access, running as a rootless VM,
  User-Mode Linux, or a chroot on rooted devices.
- **Wi-Fi security testing** — handshake and PMKID capture, deauthentication, and
  attack monitoring, built around your device's Wi-Fi adapter.
- **Network reconnaissance** — Nmap-based scanning and host/service discovery.
- **USB arsenal** — USB gadget/HID profiles (keyboard, storage, network, and more)
  for physical-access testing, with an editor for building your own device identities.
- **Remote desktop (VNC)** — a graphical desktop into the Linux environment when you
  need more than a terminal.
- **Exploit tooling** — a local exploit-db mirror, Nuclei scanning, and an MSF RPC
  console for driving Metasploit from the Linux environment.
- **Everything logged** — a built-in log viewer/exporter so you can review and keep
  records of what a session did.

## Requirements

- Android 7.0 (API 24) or newer, **arm64-v8a devices only**
- A few GB of free storage — the Linux environment (kernel + root filesystem) is
  downloaded on first run
- Root is **not required** — the rootless VM and User-Mode Linux engines work on stock,
  unrooted devices. A rooted device additionally unlocks the chroot engine.
- A device with more RAM gives the Linux environment more headroom; older/low-RAM
  devices will still work but may want to stick to the lighter engines

## Install

1. Download the latest APK from **[the XMR Lab site](https://xmr-lab-landing-page.onrender.com/)**,
   or from this repository's [Releases](../../releases) page.
2. Install it like any APK from outside the Play Store — you'll need to allow
   "install unknown apps" for your browser or file manager the first time.
3. Open the app and follow the setup screens. On first run it downloads the Linux
   kernel and root filesystem (a few hundred MB), verified against the checksums in
   [`xmr_manifest.json`](xmr_manifest.json) — this only happens once.
4. Pick an engine (rootless VM is the default and works on any supported device) and
   you're ready to go.

## License

XMR Lab is free software, licensed under the **GNU General Public License v3.0** — see
[`LICENSE`](LICENSE). It builds on several open-source projects; see the in-app
*Settings → About → Open-source licenses* screen for the full list of bundled
components and their licenses.

## Build it yourself

Most people should just install the APK above. If you want to build from source
instead, see [`BUILDING.md`](BUILDING.md).
